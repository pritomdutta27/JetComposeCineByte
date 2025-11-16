package com.pritom.domain

import app.cash.turbine.test
import com.pritom.domain.model.Movie
import com.pritom.domain.model.MovieCategory
import com.pritom.domain.repository.MovieRepository
import com.pritom.domain.usecase.GetMoviesUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.fail


class GetMoviesUseCaseTest {

    @Mock
    private lateinit var mockRepository: MovieRepository

    private lateinit var getMoviesUseCase: GetMoviesUseCase


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getMoviesUseCase = GetMoviesUseCase(mockRepository)
    }

    @Test
    fun `invoke should return flow list from repository for category POPULAR`() = runTest {
        // Given
        val category = MovieCategory.POPULAR
        val expectedMovies = listOf(
            Movie(id = 1, title = "Movie 1", posterUrl = "", rating = 2.4),
            Movie(id = 2, title = "Movie 1", posterUrl = "", rating = 2.4),
        )

        Mockito.`when`(mockRepository.getMovies(category))
            .thenReturn(flowOf(expectedMovies))

        // When
        val result = getMoviesUseCase(category)

        // Then
        result.collect { movies ->
            assertEquals(expectedMovies, movies)
        }
    }


    @Test
    fun `invoke should return flow list from repository for category NOW_PLAYING`() = runTest {
        // Given
        val category = MovieCategory.NOW_PLAYING
        val expectedMovies = listOf(
            Movie(id = 1, title = "Movie 1", posterUrl = "", rating = 2.4),
            Movie(id = 2, title = "Movie 1", posterUrl = "", rating = 2.4),
        )

        Mockito.`when`(mockRepository.getMovies(category))
            .thenReturn(flowOf(expectedMovies))

        // When
        val result = getMoviesUseCase(category)

        // Then
        result.collect { movies ->
            assertEquals(expectedMovies, movies)
        }
    }

    @Test
    fun `invoke should return flow list from repository for category TOP_RATED`() = runTest {
        // Given
        val category = MovieCategory.TOP_RATED
        val expectedMovies = listOf(
            Movie(id = 1, title = "Movie 1", posterUrl = "", rating = 2.4),
            Movie(id = 2, title = "Movie 1", posterUrl = "", rating = 2.4),
        )

        Mockito.`when`(mockRepository.getMovies(category))
            .thenReturn(flowOf(expectedMovies))

        // When
        val result = getMoviesUseCase(category)

        // Then
        result.collect { movies ->
            assertEquals(expectedMovies, movies)
        }
    }

    @Test
    fun `invoke should propagate empty list from repository For NOW_PLAYING`() = runTest {
        // Given
        val category = MovieCategory.NOW_PLAYING
        val emptyList = emptyList<Movie>()

        Mockito.`when`(mockRepository.getMovies(category))
            .thenReturn(flowOf(emptyList))

        // When
        val result = getMoviesUseCase(category)

        // Then
        result.collect { movies ->
            assertTrue(movies.isEmpty())
        }
    }

    @Test
    fun `invoke should propagate empty list from repository For POPULAR`() = runTest {
        // Given
        val category = MovieCategory.POPULAR
        val emptyList = emptyList<Movie>()

        Mockito.`when`(mockRepository.getMovies(category))
            .thenReturn(flowOf(emptyList))

        // When
        val result = getMoviesUseCase(category)

        // Then
        result.collect { movies ->
            assertTrue(movies.isEmpty())
        }
    }

    @Test
    fun `invoke should propagate empty list from repository For TOP_RATED`() = runTest {
        // Given
        val category = MovieCategory.TOP_RATED
        val emptyList = emptyList<Movie>()

        Mockito.`when`(mockRepository.getMovies(category))
            .thenReturn(flowOf(emptyList))

        // When
        val result = getMoviesUseCase(category)

        // Then
        result.collect { movies ->
            assertTrue(movies.isEmpty())
        }
    }

    @Test
    fun `invoke should propagate error flow from repository`() = runTest {
        // Given
        val category = MovieCategory.NOW_PLAYING
        val expectedException = RuntimeException("Network error")

        Mockito.`when`(mockRepository.getMovies(category))
            .thenReturn(flow { throw expectedException })

        // When
        val result = getMoviesUseCase(category)

        result.catch {
            assertEquals(expectedException.message, it.message)
            emit(emptyList())
        }.test {
            val items = awaitItem()
            Assert.assertEquals(true, items.isEmpty())
            awaitComplete()
        }
    }



}