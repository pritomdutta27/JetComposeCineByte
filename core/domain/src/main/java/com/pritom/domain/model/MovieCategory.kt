package com.pritom.domain.model

enum class MoviePosition(val position: Int) {
    First(1),
    Second(2),
    Third(3),
}

enum class MovieCategory(val category: String, val position: MoviePosition) {
    NOW_PLAYING("now_playing", MoviePosition.Third),
    TOP_RATED("top_rated", MoviePosition.First),
    POPULAR("popular", MoviePosition.Second),
}

fun Map<MovieCategory, List<Movie>>.sortedByPosition(): Map<MovieCategory, List<Movie>> {
    val sorted = this.toList()
        .sortedBy { (category, _) -> category.position.position }
        .associateTo(LinkedHashMap()) { it.first to it.second }
//    println("Sorted categories: ${sorted.keys.map { it to it.position.position }}")
    return sorted
}