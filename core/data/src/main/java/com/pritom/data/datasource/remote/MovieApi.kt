package com.pritom.data.datasource.remote

import com.pritom.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("3/movie/{type}?language=en-US")
    suspend fun getMovieList(
        @Path("type") type: String = "now_playing",
        @Query("page") page: Int = 1
    ): MovieResponse
}