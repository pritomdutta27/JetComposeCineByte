package com.pritom.data.di

import androidx.tracing.trace
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor



@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun providesOkhttpClientFactory(): Call.Factory = trace("CineByteOkHttpClient") {
        OkHttpClient.Builder().addInterceptor (
            HttpLoggingInterceptor()
                .apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
//                    if (BuildConfig.DEBUG) {
//
//                    }
                })
        }.build()
}