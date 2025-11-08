package com.pritom.domain.repository

import com.pritom.domain.model.Movie
import com.pritom.domain.model.MovieCategory

interface MovieRepository {
    suspend fun getMovies(category: MovieCategory): List<Movie>
}