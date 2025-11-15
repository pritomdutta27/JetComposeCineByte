package com.pritom.data.repository

import androidx.annotation.OpenForTesting
import com.pritom.data.datasource.remote.apistrategies.MovieCategoryStrategy
import com.pritom.domain.model.Movie
import com.pritom.domain.model.MovieCategory
import com.pritom.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OpenForTesting
class MovieRepositoryImpl @Inject constructor(
    private val strategies: Map<MovieCategory, MovieCategoryStrategy>
) : MovieRepository {

    override suspend fun getMovies(category: MovieCategory): Flow<List<Movie>> {
        val strategy = strategies[category]
            ?: throw IllegalArgumentException("Unknown movie category: $category")
        return strategy.fetchMovies()
    }
}