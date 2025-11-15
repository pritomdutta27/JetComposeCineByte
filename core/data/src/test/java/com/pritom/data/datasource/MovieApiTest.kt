package com.pritom.data.datasource

import com.pritom.data.datasource.remote.MovieApi
import com.pritom.data.helper.Helper
import com.pritom.domain.model.MovieCategory
import junit.framework.TestCase.fail
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

class MovieApiTest {

    private lateinit var mockWebServer: MockWebServer

    private lateinit var movieApi: MovieApi

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        movieApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build().create(MovieApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun test_get_Now_Playing_IsEmpty() = runTest {
        mockResponseForEmptyList()
        val response = movieApi.getMovieList(type = MovieCategory.NOW_PLAYING.category)
        mockWebServer.takeRequest()
        Assert.assertEquals(true, response.results.isEmpty())
    }

    @Test
    fun test_get_POPULAR_movie_response_list_is_empty() = runTest {
        mockResponseForEmptyList()
        val response = movieApi.getMovieList(type = MovieCategory.POPULAR.category)
        mockWebServer.takeRequest()
        Assert.assertEquals(true, response.results.isEmpty())
    }

    @Test
    fun test_get_TOP_RATED_movie_response_list_is_empty() = runTest {
        mockResponseForEmptyList()
        val response = movieApi.getMovieList(type = MovieCategory.TOP_RATED.category)
        mockWebServer.takeRequest()
        Assert.assertEquals(true, response.results.isEmpty())
    }

    @Test
    fun test_get_NOW_PLAYING_movies_return_results_list() = runTest {
        // Arrange
        mockResponseWithList()
        // Act
        val response = movieApi.getMovieList(type = MovieCategory.NOW_PLAYING.category)
        mockWebServer.takeRequest()
        //Assert
        Assert.assertEquals(false, response.results.isEmpty())
        Assert.assertEquals(4, response.results.size)
    }

    @Test
    fun test_get_POPULAR_movies_return_results_list() = runTest {
        // Arrange
        mockResponseWithList()
        // Act
        val response = movieApi.getMovieList(type = MovieCategory.POPULAR.category)
        mockWebServer.takeRequest()
        //Assert
        Assert.assertEquals(false, response.results.isEmpty())
        Assert.assertEquals(4, response.results.size)
    }

    @Test
    fun test_get_TOP_RATED_movies_return_results_list() = runTest {
        // Arrange
        mockResponseWithList()
        // Act
        val response = movieApi.getMovieList(type = MovieCategory.TOP_RATED.category)
        mockWebServer.takeRequest()
        //Assert
        Assert.assertEquals(false, response.results.isEmpty())
        Assert.assertEquals(4, response.results.size)
    }


    @Test
    fun test_get_404_error_from_movies_api() = runTest{
        //Arrange
        mockResponseErrorCode(errorCode = 404)
        //Act & Assert
        try {
            movieApi.getMovieList("invalid", 1)
            mockWebServer.takeRequest()
            fail("Expected HttpException")
        } catch (e: HttpException) {
            Assert.assertEquals(404, e.code())
        }
    }

    @Test
    fun test_get_500_error_from_movies_api() = runTest{
        //Arrange
        mockResponseErrorCode(errorCode = 500)
        //Act & Assert
        try {
            movieApi.getMovieList("invalid", 1)
            mockWebServer.takeRequest()
            fail("Expected HttpException")
        } catch (e: HttpException) {
            Assert.assertEquals(500, e.code())
        }
    }

    //TODO:Arrange Sever Response
    private fun mockResponseForEmptyList(){
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/movie_empty_list.json")
        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)
        mockWebServer.enqueue(mockResponse)
    }

    private fun mockResponseWithList(){
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/api_response_data.json")
        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)
        mockWebServer.enqueue(mockResponse)
    }

    private fun mockResponseErrorCode(errorCode: Int){
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/error.json")
        mockResponse.setResponseCode(errorCode)
        mockResponse.setBody(content)
        mockWebServer.enqueue(mockResponse)
        //Act & Assert
    }

}