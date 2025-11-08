package com.pritom.data.datasource.remote.apistrategies

import com.pritom.domain.model.Movie

interface MovieCategoryStrategy {
    suspend fun fetchMovies(): List<Movie>

}