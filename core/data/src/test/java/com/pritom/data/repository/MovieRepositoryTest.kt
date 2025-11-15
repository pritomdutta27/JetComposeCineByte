package com.pritom.data.repository

import app.cash.turbine.test
import com.pritom.data.helper.Helper
import com.pritom.data.model.ApiException
import com.pritom.domain.model.MovieCategory
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MovieRepositoryTest {

    @Mock
    lateinit var sutApi: MovieRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }


    @Test
    fun get_NOW_PLAYING_Movies_EmptyList() = runTest {
        emptyList(MovieCategory.NOW_PLAYING)
        val response = sutApi.getMovies(MovieCategory.NOW_PLAYING).first()
        assertEquals(true, response.isEmpty())
        assertEquals(0, response.size)
    }

    @Test
    fun get_POPULAR_Movies_EmptyList() = runTest {
        emptyList(MovieCategory.POPULAR)
        val response = sutApi.getMovies(MovieCategory.POPULAR).first()
        assertEquals(true, response.isEmpty())
        assertEquals(0, response.size)
    }

    @Test
    fun get_TOP_RATED_Movies_EmptyList() = runTest {
        emptyList(MovieCategory.TOP_RATED)
        val response = sutApi.getMovies(MovieCategory.TOP_RATED).first()
        assertEquals(true, response.isEmpty())
        assertEquals(0, response.size)
    }


    @Test
    fun get_NOW_PLAYING_Movies_4_Items() = runTest {
        success(MovieCategory.NOW_PLAYING)
        val response = sutApi.getMovies(MovieCategory.NOW_PLAYING).first()
        assertEquals(false, response.isEmpty())
        assertEquals(4, response.size)
    }

    @Test
    fun get_POPULAR_Movies_4_Items() = runTest {
        success(MovieCategory.POPULAR)
        val response = sutApi.getMovies(MovieCategory.POPULAR).first()
        assertEquals(false, response.isEmpty())
        assertEquals(4, response.size)
    }

    @Test
    fun get_TOP_RATED_Movies_4_Items() = runTest {
        success(MovieCategory.TOP_RATED)
        val response = sutApi.getMovies(MovieCategory.TOP_RATED).first()
        assertEquals(false, response.isEmpty())
        assertEquals(4, response.size)
    }


    @Test
    fun test_api_error_for_NOW_PLAYING() = runTest {
        // Arrange
        failure(MovieCategory.NOW_PLAYING) // Mock the failure
        // Act & Assert
        sutApi.getMovies(MovieCategory.NOW_PLAYING)
            .catch { error ->
                // Assert on the error in catch block
//                println("${error is ApiException}")
                assertEquals(true, error is ApiException)
                assertEquals(401, (error as? ApiException)?.statusCode)
                assertEquals("Unauthorized", (error as? ApiException)?.message)
                emit(emptyList()) // Continue with empty list
            }
            .test {
                val items = awaitItem()
                assertEquals(true, items.isEmpty())
                awaitComplete()
            }
    }

    @Test
    fun test_api_error_for_POPULAR() = runTest {
        // Arrange
        failure(MovieCategory.POPULAR) // Mock the failure
        // Act & Assert
        sutApi.getMovies(MovieCategory.POPULAR)
            .catch { error ->
                // Assert on the error in catch block
//                println("${error is ApiException}")
                assertEquals(true, error is ApiException)
                assertEquals(401, (error as? ApiException)?.statusCode)
                assertEquals("Unauthorized", (error as? ApiException)?.message)
                emit(emptyList()) // Continue with empty list
            }
            .test {
                val items = awaitItem()
                assertEquals(true, items.isEmpty())
                awaitComplete()
            }
    }

    fun test_api_error_for_TOP_RATED() = runTest {
        // Arrange
        failure(MovieCategory.TOP_RATED) // Mock the failure
        // Act & Assert
        sutApi.getMovies(MovieCategory.TOP_RATED)
            .catch { error ->
                // Assert on the error in catch block
//                println("${error is ApiException}")
                assertEquals(true, error is ApiException)
                assertEquals(401, (error as? ApiException)?.statusCode)
                assertEquals("Unauthorized", (error as? ApiException)?.message)
                emit(emptyList()) // Continue with empty list
            }
            .test {
                val items = awaitItem()
                assertEquals(true, items.isEmpty())
                awaitComplete()
            }
    }



    private suspend fun success(category: MovieCategory) {
        val testData = Helper.getTestData("api_response_data.json")
        Mockito.`when`(sutApi.getMovies(category)).thenReturn(
            flow { emit(testData.results.map { it.toDomain() }) }
        )
    }

    private suspend fun failure(category: MovieCategory) {
        Mockito.`when`(sutApi.getMovies(category)).thenReturn(
            flow {
                throw ApiException(401, null, "Unauthorized")
            }
        )
    }

    private suspend fun emptyList(category: MovieCategory) {
        val testData = Helper.getTestData("movie_empty_list.json")
        Mockito.`when`(sutApi.getMovies(category)).thenReturn(
            flow { emit(testData.results.map { it.toDomain() }) }
        )
    }
}