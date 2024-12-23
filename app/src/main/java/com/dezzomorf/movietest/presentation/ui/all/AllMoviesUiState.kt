package com.dezzomorf.movietest.presentation.ui.all

import com.dezzomorf.movietest.domain.model.Movie

data class AllMoviesUiState(
    val moviesByMonth: Map<String, List<Movie>> = emptyMap(),
    val currentPage: Int = 0,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isError: Boolean = false
)
