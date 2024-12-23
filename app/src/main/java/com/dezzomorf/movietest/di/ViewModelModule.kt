package com.dezzomorf.movietest.di

import com.dezzomorf.movietest.presentation.ui.all.AllMoviesViewModel
import com.dezzomorf.movietest.presentation.ui.favorites.FavoritesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AllMoviesViewModel(
            getMoviesUseCase = get(),
            toggleFavoriteUseCase = get(),
            fetchMoviesUseCase = get(),
            resourceManager = get(),
        )
    }
    viewModel {
        FavoritesViewModel(
            toggleFavoriteUseCase = get(),
            getFavouriteMoviesUseCase = get(),
            resourceManager = get(),
        )
    }
}
