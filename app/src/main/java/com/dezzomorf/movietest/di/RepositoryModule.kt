package com.dezzomorf.movietest.di

import com.dezzomorf.movietest.data.repository.MovieRepositoryImpl
import com.dezzomorf.movietest.domain.repository.MovieRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<MovieRepository> {
        MovieRepositoryImpl(
            movieDao = get(),
            tmdbApi = get()
        )
    }
}
