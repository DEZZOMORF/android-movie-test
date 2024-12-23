package com.dezzomorf.movietest.presentation.ui.all

import androidx.lifecycle.viewModelScope
import com.dezzomorf.movietest.domain.usecase.FetchMoviesUseCase
import com.dezzomorf.movietest.domain.usecase.GetMoviesUseCase
import com.dezzomorf.movietest.domain.usecase.ToggleFavoriteUseCase
import com.dezzomorf.movietest.presentation.ui.BaseViewModel
import com.dezzomorf.movietest.utils.ResourceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AllMoviesViewModel(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val fetchMoviesUseCase: FetchMoviesUseCase,
    resourceManager: ResourceManager,
) : BaseViewModel(resourceManager) {

    private val _uiState = MutableStateFlow(AllMoviesUiState())
    val uiState: StateFlow<AllMoviesUiState> = _uiState

    init {
        executeMovies()
        loadFirstPage()
    }

    private fun executeMovies() {
        viewModelScope.launch {
            getMoviesUseCase.execute().collect { movies ->
                _uiState.update {
                    it.copy(
                        moviesByMonth = groupMoviesByMonth(movies),
                        isLoading = false,
                        isError = false
                    )
                }
            }
        }
    }

    private fun loadFirstPage() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = fetchMoviesUseCase.fetchMovies(1)
            _uiState.update {
                if (result.isSuccess) {
                    it.copy(currentPage = 1, isLoading = false, isError = false)
                } else {
                    it.copy(isLoading = false, isError = true)
                }
            }
        }
    }

    fun refreshMovies() {
        _uiState.update { it.copy(isRefreshing = true) }
        viewModelScope.launch {
            val result = fetchMoviesUseCase.fetchMovies(1)
            _uiState.update {
                if (result.isSuccess) {
                    it.copy(isRefreshing = false, isError = false)
                } else {
                    it.copy(isRefreshing = false, isError = true)
                }
            }
        }
    }

    fun loadMore() {
        val current = _uiState.value
        if (!current.isLoadingMore) {
            _uiState.update { it.copy(isLoadingMore = true) }
            viewModelScope.launch {
                val nextPage = current.currentPage + 1
                val result = fetchMoviesUseCase.fetchMovies(nextPage)
                _uiState.update {
                    if (result.isSuccess) {
                        it.copy(currentPage = nextPage, isLoadingMore = false)
                    } else {
                        it.copy(isLoadingMore = false, isError = true)
                    }
                }
            }
        }
    }

    fun onToggleFavorite(movieId: Long, currentIsFavorite: Boolean) {
        viewModelScope.launch {
            toggleFavoriteUseCase.execute(movieId, !currentIsFavorite)
        }
    }
}
