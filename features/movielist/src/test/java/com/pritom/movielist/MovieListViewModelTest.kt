package com.pritom.movielist

import app.cash.turbine.test
import com.pritom.domain.model.Movie
import com.pritom.domain.model.MovieCategory
import com.pritom.domain.usecase.GetMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.assertTrue


class MovieListViewModelTest {

    @Mock
    private lateinit var mockGetAllMoviesUseCase: GetMoviesUseCase

    private lateinit var viewModel: MovieListViewModel

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = MovieListViewModel(mockGetAllMoviesUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadAllMovies should load movies for all categories`() = runTest {
        // Given
        val expectedMovies = mapOf(
            MovieCategory.POPULAR to listOf(
                Movie(
                    id = 1,
                    title = "Popular Movie",
                    posterUrl = "",
                    rating = 2.4
                )
            ),
            MovieCategory.TOP_RATED to listOf(
                Movie(
                    id = 2,
                    title = "Top Rated Movie",
                    posterUrl = "",
                    rating = 2.4
                )
            ),
            MovieCategory.NOW_PLAYING to listOf(
                Movie(
                    id = 4,
                    title = "Now Playing Movie",
                    posterUrl = "",
                    rating = 2.4
                )
            )
        )

        // Mock each category to return corresponding movies
        MovieCategory.entries.forEach { category ->
            Mockito.`when`(mockGetAllMoviesUseCase(category))
                .thenReturn(flowOf(expectedMovies[category] ?: emptyList()))
        }

        // Then
        viewModel.moviesByCategory.test {
            // Skip initial empty state
            skipItems(1)
            // When
            viewModel.loadAllMovies()
            testDispatcher.scheduler.advanceUntilIdle()

            // Collect just last emit
            var finalState: Map<MovieCategory, List<Movie>> = mutableMapOf()
            repeat(MovieCategory.entries.size) {
                finalState = awaitItem()
            }
            assertEquals(expectedMovies.size, finalState.size)
            expectedMovies.forEach { (category, expectedMovieList) ->
                assertEquals(expectedMovieList, finalState[category])
            }
            // Ensure no more emissions
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `load empty list for category`() = runTest {
        // Given
        val expectedMovies = mapOf(
            MovieCategory.POPULAR to emptyList<Movie>(),
            MovieCategory.TOP_RATED to emptyList<Movie>(),
            MovieCategory.NOW_PLAYING to emptyList<Movie>()
        )

        // Mock each category to return corresponding movies
        MovieCategory.entries.forEach { category ->
            Mockito.`when`(mockGetAllMoviesUseCase(category))
                .thenReturn(flowOf(expectedMovies[category] ?: emptyList()))
        }

        // Then
        viewModel.moviesByCategory.test {
            // Skip initial empty state
            skipItems(1)
            // When
            viewModel.loadAllMovies()
            testDispatcher.scheduler.advanceUntilIdle()

            // Collect just last emit
            var finalState: Map<MovieCategory, List<Movie>> = mutableMapOf()
            repeat(MovieCategory.entries.size) {
                finalState = awaitItem()
            }
            assertEquals(expectedMovies.size, finalState.size)
            expectedMovies.forEach { (category, expectedMovieList) ->
                assertEquals(expectedMovieList, finalState[category])
            }
            // Ensure no more emissions
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `load empty list for Now Playing category`() = runTest {
        // Given
        val expectedMovies = mapOf(
            MovieCategory.POPULAR to listOf(
                Movie(
                    id = 1,
                    title = "Popular Movie",
                    posterUrl = "",
                    rating = 2.4
                )
            ),
            MovieCategory.TOP_RATED to listOf(
                Movie(
                    id = 2,
                    title = "Top Rated Movie",
                    posterUrl = "",
                    rating = 2.4
                )
            ),
            MovieCategory.NOW_PLAYING to emptyList<Movie>()
        )

        // Mock each category to return corresponding movies
        MovieCategory.entries.forEach { category ->
            Mockito.`when`(mockGetAllMoviesUseCase(category))
                .thenReturn(flowOf(expectedMovies[category] ?: emptyList()))
        }

        // Then
        viewModel.moviesByCategory.test {
            // Skip initial empty state
            skipItems(1)
            // When
            viewModel.loadAllMovies()
            testDispatcher.scheduler.advanceUntilIdle()

            // Collect just last emit
            var finalState: Map<MovieCategory, List<Movie>> = mutableMapOf()
            repeat(MovieCategory.entries.size) {
                finalState = awaitItem()
            }
            assertEquals(expectedMovies.size, finalState.size)
            expectedMovies.forEach { (category, expectedMovieList) ->
                assertEquals(expectedMovieList, finalState[category])
            }
            // Ensure no more emissions
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `loadAllMovies should handle errors gracefully, get error on POPULAR api`() = runTest {
        // Given
        val successfulMovies = listOf(
            Movie(
                id = 1,
                title = "Successful Movie",
                posterUrl = "",
                rating = 2.4
            )
        )

        // Make one category fail
        val failingCategory = MovieCategory.POPULAR

        // Mock successful categories
        MovieCategory.entries.forEach { category ->
            if (category == failingCategory) {
                Mockito.`when`(mockGetAllMoviesUseCase(category))
                    .thenReturn(flow { throw RuntimeException("Network error") })
            } else {
                Mockito.`when`(mockGetAllMoviesUseCase(category))
                    .thenReturn(flowOf(successfulMovies))
            }
        }

        viewModel.moviesByCategory.test {
            // Skip initial empty state
            skipItems(1)

            // When
            viewModel.loadAllMovies()
            testDispatcher.scheduler.advanceUntilIdle()

            // Collect all updates from successful categories
            val allUpdates = mutableListOf<Map<MovieCategory, List<Movie>>>()
            repeat(MovieCategory.entries.size - 1) { // One less because one category failed
                allUpdates.add(awaitItem())
            }

            // Then - Check the final state (should have all successful categories)
            val finalState = allUpdates.last()

            assertEquals(MovieCategory.entries.size - 1, finalState.size) // One category missing

            // Verify failing category is not in the result
            assertFalse(finalState.containsKey(failingCategory))

            // Verify successful categories are present
            MovieCategory.entries.forEach { category ->
                if (category != failingCategory) {
                    assertEquals(successfulMovies, finalState[category])
                }
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `get all movie api error`() = runTest {
        // Mock successful categories
        MovieCategory.entries.forEach { category ->
            Mockito.`when`(mockGetAllMoviesUseCase(category))
                .thenReturn(flow { throw RuntimeException("Network error") })
        }

        viewModel.moviesByCategory.test {
            // Skip initial empty state
            skipItems(1)

            // When
            viewModel.loadAllMovies()
            testDispatcher.scheduler.advanceUntilIdle()

            //TODO: Error and Loading state not handled yet it should handle then test view model class
            // Then - No new emissions because all categories failed and errors are ignored
            // Just verify the current state is still empty
            expectNoEvents()

            // Additional assertion to verify state is empty
            assertEquals(0, viewModel.moviesByCategory.value.size)
            assertTrue(viewModel.moviesByCategory.value.isEmpty())
        }
    }

}