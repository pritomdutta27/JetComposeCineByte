package com.pritom.data.datasource.remote.apistrategies

import com.pritom.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieCategoryStrategy {
    suspend fun fetchMovies(): Flow<List<Movie>>

}