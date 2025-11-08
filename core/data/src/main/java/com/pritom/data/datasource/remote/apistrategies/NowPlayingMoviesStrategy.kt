package com.pritom.data.datasource.remote.apistrategies

import com.pritom.data.datasource.remote.MovieApi
import com.pritom.domain.model.MovieCategory
import com.pritom.domain.model.Movie
import javax.inject.Inject

class NowPlayingMoviesStrategy @Inject constructor(private val api: MovieApi) : MovieCategoryStrategy {
    override suspend fun fetchMovies(): List<Movie> {
        return api.getMovieList(type = MovieCategory.NOW_PLAYING.category).results.map { it.toDomain() }
    }
}