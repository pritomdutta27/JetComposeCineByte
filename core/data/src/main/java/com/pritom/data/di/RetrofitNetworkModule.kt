package com.pritom.data.di

import androidx.tracing.trace
import com.pritom.data.datasource.remote.MovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object RetrofitNetworkModule {

    @Provides
    @Singleton
    fun providesRetrofit(
        jsonNetwork: Json,
        okhttpCallFactory: dagger.Lazy<Call.Factory>,
    ) = trace("RetrofitCineByteNetwork") {
        Retrofit.Builder()
            .baseUrl("")
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                jsonNetwork.asConverterFactory("application/json".toMediaType()),
            )
            .build()
            .create(MovieApi::class.java)
    }
}