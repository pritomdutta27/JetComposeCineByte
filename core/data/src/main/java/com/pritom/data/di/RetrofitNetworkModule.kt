package com.pritom.data.di

import androidx.tracing.trace
import com.pritom.data.datasource.remote.ApiExceptionCallAdapterFactory
import com.pritom.data.datasource.remote.MovieApi
import com.pritom.data.datasource.remote.apistrategies.MovieCategoryStrategy
import com.pritom.data.datasource.remote.apistrategies.NowPlayingMoviesStrategy
import com.pritom.data.datasource.remote.apistrategies.PopularMoviesStrategy
import com.pritom.domain.model.MovieCategory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton
import kotlin.text.get

@InstallIn(SingletonComponent::class)
@Module
object RetrofitNetworkModule {


//    @Provides
//    @Singleton
//    fun providesStrategy(api: MovieApi): Map<MovieCategory, MovieCategoryStrategy> {
//        return mapOf(
//            MovieCategory.POPULAR to PopularMoviesStrategy(api),
//            MovieCategory.NOW_PLAYING to NowPlayingMoviesStrategy(api),
//        )
//    }

    @Provides
    @Singleton
    fun provideRetrofit(
        jsonNetwork: Json,
        okhttpCallFactory: dagger.Lazy<Call.Factory>
    ): MovieApi = trace("CineByteRetrofit"){
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                jsonNetwork.asConverterFactory("application/json".toMediaType()),
            )
            .addCallAdapterFactory(ApiExceptionCallAdapterFactory())
            .build()
            .create(MovieApi::class.java)
    }


}