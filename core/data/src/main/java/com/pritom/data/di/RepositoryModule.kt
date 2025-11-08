package com.pritom.data.di

import com.pritom.data.datasource.remote.MovieApi
import com.pritom.data.datasource.remote.apistrategies.NowPlayingMoviesStrategy
import com.pritom.data.datasource.remote.apistrategies.PopularMoviesStrategy
import com.pritom.data.repository.MovieRepositoryImpl
import com.pritom.domain.model.MovieCategory
import com.pritom.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(api: MovieApi): MovieRepository {
        val strategies = mapOf(
            MovieCategory.POPULAR to PopularMoviesStrategy(api),
            MovieCategory.NOW_PLAYING to NowPlayingMoviesStrategy(api),
        )
        return MovieRepositoryImpl(strategies)
    }
}