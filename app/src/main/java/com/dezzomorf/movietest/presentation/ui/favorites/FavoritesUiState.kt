package com.dezzomorf.movietest.presentation.ui.favorites

import com.dezzomorf.movietest.domain.model.Movie

data class FavoritesUiState(
    val favoritesByMonth: Map<String, List<Movie>> = emptyMap(),
    val isEmpty: Boolean = true
)
