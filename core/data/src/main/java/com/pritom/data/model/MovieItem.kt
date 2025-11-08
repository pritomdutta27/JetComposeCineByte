package com.pritom.data.model

import com.pritom.domain.model.Movie

data class MovieItem(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
){
    fun toDomain() = Movie(
        id = id,
        title = original_title,
        posterUrl = poster_path,
        rating = vote_average
    )
}