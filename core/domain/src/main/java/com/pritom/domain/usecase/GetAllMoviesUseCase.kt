package com.pritom.domain.usecase

import com.pritom.domain.model.Movie
import com.pritom.domain.model.MovieCategory
import com.pritom.domain.repository.MovieRepository
import javax.inject.Inject

class GetAllMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(): Map<MovieCategory, List<Movie>> {
        return MovieCategory.entries.associateWith { category ->
            repository.getMovies(category)
        }
    }
}