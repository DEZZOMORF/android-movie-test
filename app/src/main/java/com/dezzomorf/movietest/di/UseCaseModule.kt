package com.dezzomorf.movietest.di

import com.dezzomorf.movietest.domain.repository.MovieRepository
import com.dezzomorf.movietest.domain.usecase.FetchMoviesUseCase
import com.dezzomorf.movietest.domain.usecase.GetFavouriteMoviesUseCase
import com.dezzomorf.movietest.domain.usecase.GetMoviesUseCase
import com.dezzomorf.movietest.domain.usecase.ToggleFavoriteUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetMoviesUseCase(get<MovieRepository>()) }
    factory { GetFavouriteMoviesUseCase(get<MovieRepository>()) }
    factory { ToggleFavoriteUseCase(get<MovieRepository>()) }
    factory { FetchMoviesUseCase(get<MovieRepository>()) }
}
