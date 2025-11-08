package com.pritom.data.model

data class MovieResponse(
    val page: Int,
    val results: List<PopularMovie>,
    val total_pages: Int,
    val total_results: Int
)