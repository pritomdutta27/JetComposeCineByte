package com.pritom.data.repository

import com.pritom.data.datasource.remote.apistrategies.MovieCategoryStrategy
import com.pritom.domain.model.Movie
import com.pritom.domain.model.MovieCategory
import com.pritom.domain.repository.MovieRepository

class MovieRepositoryImpl(
    private val strategies: Map<MovieCategory, MovieCategoryStrategy>
) : MovieRepository {

    override suspend fun getMovies(category: MovieCategory): List<Movie> {
        val strategy = strategies[category]
            ?: throw IllegalArgumentException("Unknown movie category: $category")
        return strategy.fetchMovies()
    }
}