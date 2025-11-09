package com.pritom.data.datasource.remote.apistrategies

import com.pritom.data.datasource.remote.MovieApi
import com.pritom.domain.model.Movie
import com.pritom.domain.model.MovieCategory
import javax.inject.Inject

class TopRatedMoviesStrategy @Inject constructor(private val api: MovieApi) : MovieCategoryStrategy {
    override suspend fun fetchMovies(): List<Movie> {
        return api.getMovieList(type = MovieCategory.TOP_RATED.category).results.map { it.toDomain() }
    }
}