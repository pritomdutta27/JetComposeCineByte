package com.pritom.data.datasource.remote.apistrategies

import com.pritom.data.datasource.remote.MovieApi
import com.pritom.domain.model.MovieCategory
import com.pritom.domain.model.Movie

class PopularMoviesStrategy(private val api: MovieApi) : MovieCategoryStrategy {
    override suspend fun fetchMovies(): List<Movie> {
        return api.getMovieList(type = MovieCategory.POPULAR.category).results.map { it.toDomain() }
    }
}