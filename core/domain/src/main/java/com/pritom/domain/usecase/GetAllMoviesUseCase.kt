package com.pritom.domain.usecase

import com.pritom.domain.model.Movie
import com.pritom.domain.model.MovieCategory
import com.pritom.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(category: MovieCategory): Flow<List<Movie>> = flow {
        emit(repository.getMovies(category))
    }
}