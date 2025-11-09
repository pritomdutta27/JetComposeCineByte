package com.pritom.data.di

import androidx.tracing.trace
import com.pritom.data.datasource.remote.DataSourceConstants
import com.pritom.data.utils.CustomHttpLogging
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
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Authorization", "Bearer ${DataSourceConstants.API_KEY}")
                    .header("Content-Type", "application/json")
                    .header("User-Agent", "CineByte/1.0")
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(
                HttpLoggingInterceptor(CustomHttpLogging(logName = "CineByteOkHttpClient"))
                    .apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
//                    if (BuildConfig.DEBUG) {
//
//                    }
                    })
    }.build()
}