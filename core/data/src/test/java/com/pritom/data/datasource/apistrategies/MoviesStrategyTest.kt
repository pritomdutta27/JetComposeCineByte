package com.pritom.data.datasource.apistrategies

import app.cash.turbine.test
import com.pritom.data.datasource.remote.MovieApi
import com.pritom.data.datasource.remote.apistrategies.NowPlayingMoviesStrategy
import com.pritom.data.datasource.remote.apistrategies.PopularMoviesStrategy
import com.pritom.data.datasource.remote.apistrategies.TopRatedMoviesStrategy
import com.pritom.data.helper.Helper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class MoviesStrategyTest {

    private lateinit var mockWebServer: MockWebServer

    private lateinit var movieApi: MovieApi

    private lateinit var nowPlayingMoviesStrategy: NowPlayingMoviesStrategy
    private lateinit var popularMoviesStrategy: PopularMoviesStrategy
    private lateinit var topRatedMoviesStrategy: TopRatedMoviesStrategy

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        movieApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
//            .addCallAdapterFactory(ApiExceptionCallAdapterFactory())
            .build()
            .create(MovieApi::class.java)

        nowPlayingMoviesStrategy = NowPlayingMoviesStrategy(movieApi)
        popularMoviesStrategy = PopularMoviesStrategy(movieApi)
        topRatedMoviesStrategy = TopRatedMoviesStrategy(movieApi)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun test_NOW_PLAYING_movies_strategy_is_get_empty_list() = runTest {
        //Arrange
        serverGetEmptyList()
        //Act
        val movies = nowPlayingMoviesStrategy.fetchMovies()
        //Assert
        movies.test {
            val result = awaitItem()
            Assert.assertEquals(true, result.isEmpty())
            awaitComplete()
        }
        mockWebServer.takeRequest()
    }

    @Test
    fun test_POPULAR_movies_strategy_is_get_empty_list() = runTest {
        //Arrange
        serverGetEmptyList()
        //Act
        val movies = popularMoviesStrategy.fetchMovies()
        //Assert
        movies.test {
            val result = awaitItem()
            Assert.assertEquals(true, result.isEmpty())
            awaitComplete()
        }
        mockWebServer.takeRequest()
    }

    @Test
    fun test_TOP_RATED_movies_strategy_is_get_empty_list() = runTest {
        //Arrange
        serverGetEmptyList()
        //Act
        val movies = topRatedMoviesStrategy.fetchMovies()
        //Assert
        movies.test {
            val result = awaitItem()
            Assert.assertEquals(true, result.isEmpty())
            awaitComplete()
        }
        mockWebServer.takeRequest()
    }


    @Test
    fun test_NOW_PLAYING_movies_strategy_is_get_list() = runTest {
        //Arrange
        serverGetMovieList()
        //Act
        val movies = nowPlayingMoviesStrategy.fetchMovies()
        //Assert
        movies.test {
            val result = awaitItem()
            Assert.assertEquals(false, result.isEmpty())
            Assert.assertEquals(4, result.size)
            Assert.assertEquals("Godzilla Minus One", result[0].title)
            awaitComplete()
        }
        mockWebServer.takeRequest()
    }

    @Test
    fun test_POPULAR_movies_strategy_is_get_list() = runTest {
        //Arrange
        serverGetMovieList()
        //Act
        val movies = popularMoviesStrategy.fetchMovies()
        //Assert
        movies.test {
            val result = awaitItem()
            Assert.assertEquals(false, result.isEmpty())
            Assert.assertEquals(4, result.size)
            Assert.assertEquals("Godzilla Minus One", result[0].title)
            awaitComplete()
        }
        mockWebServer.takeRequest()
    }

    @Test
    fun test_TOP_RATED_movies_strategy_is_get_list() = runTest {
        //Arrange
        serverGetMovieList()
        //Act
        val movies = topRatedMoviesStrategy.fetchMovies()
        //Assert
        movies.test {
            val result = awaitItem()
            Assert.assertEquals(false, result.isEmpty())
            Assert.assertEquals(4, result.size)
            Assert.assertEquals("Godzilla Minus One", result[0].title)
            awaitComplete()
        }
        mockWebServer.takeRequest()
    }

    @Test
    fun test_get_404_error_from_movie_api() = runTest {
        //Arrange
        serverGetErrorFromMovieApi(404)
        //Act
        // Act & Assert - Using catch in test
        popularMoviesStrategy.fetchMovies()
            .catch { error ->
                // Assert on the error in catch block
                Assert.assertEquals("HTTP 404 Client Error", (error as? HttpException)?.message)
                Assert.assertEquals(true, error is HttpException)
                Assert.assertEquals(404, (error as HttpException).code())
                emit(emptyList()) // Continue with empty list
            }
            .test {
                val items = awaitItem()
                Assert.assertEquals(true, items.isEmpty())
                awaitComplete()
            }
        mockWebServer.takeRequest()
    }

    //    @Test(expected = ApiException::class)
    @Test
    fun test_get_500_error_from_movie_api() = runTest {
        //Arrange
        serverGetErrorFromMovieApi(500)
        //Act
        // Act & Assert - Using catch in test
        nowPlayingMoviesStrategy.fetchMovies()
            .catch { error ->
                // Assert on the error in catch block
                Assert.assertEquals("HTTP 500 Server Error", (error as? HttpException)?.message)
                Assert.assertEquals(500, (error as HttpException).code())
                emit(emptyList()) // Continue with empty list
            }
            .test {
                val items = awaitItem()
                Assert.assertEquals(true, items.isEmpty())
                awaitComplete()
            }
        mockWebServer.takeRequest()
    }


    //TODO: Arrange Sever Response
    private fun serverGetEmptyList() {
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/movie_empty_list.json")
        mockResponse.setBody(content)
        mockResponse.setResponseCode(200)
        mockWebServer.enqueue(mockResponse)
    }

    private fun serverGetMovieList() {
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/api_response_data.json")
        mockResponse.setBody(content)
        mockResponse.setResponseCode(200)
        mockWebServer.enqueue(mockResponse)
    }

    private fun serverGetErrorFromMovieApi(errorCode: Int) {
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/error.json")
        mockResponse.setBody(content)
        mockResponse.setResponseCode(errorCode)
        mockWebServer.enqueue(mockResponse)
    }

}