package com.pritom.domain.repository

import com.pritom.domain.model.Movie
import com.pritom.domain.model.MovieCategory
import kotlinx.coroutines.flow.Flow


interface MovieRepository {
    suspend fun getMovies(category: MovieCategory): Flow<List<Movie>>
}