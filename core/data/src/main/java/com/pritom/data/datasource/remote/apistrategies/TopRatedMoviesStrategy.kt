package com.pritom.data.datasource.remote.apistrategies

import com.pritom.data.datasource.remote.MovieApi
import com.pritom.domain.model.Movie
import com.pritom.domain.model.MovieCategory
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TopRatedMoviesStrategy @Inject constructor(private val api: MovieApi) :
    MovieCategoryStrategy {
    override suspend fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            emit(
                api.getMovieList(type = MovieCategory.NOW_PLAYING.category)
                    .results.map { it.toDomain() }
            )
        }.flowOn(IO)
    }
}