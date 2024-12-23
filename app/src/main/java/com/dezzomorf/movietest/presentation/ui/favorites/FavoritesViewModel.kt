package com.dezzomorf.movietest.presentation.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezzomorf.movietest.R
import com.dezzomorf.movietest.domain.model.Movie
import com.dezzomorf.movietest.domain.repository.MovieRepository
import com.dezzomorf.movietest.domain.usecase.GetFavouriteMoviesUseCase
import com.dezzomorf.movietest.domain.usecase.ToggleFavoriteUseCase
import com.dezzomorf.movietest.presentation.ui.BaseViewModel
import com.dezzomorf.movietest.utils.ResourceManager
import com.dezzomorf.movietest.utils.TimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavouriteMoviesUseCase: GetFavouriteMoviesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    resourceManager: ResourceManager,
) : BaseViewModel(resourceManager) {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState

    init {
        getFavoriteMoviesStream()
    }

    private fun getFavoriteMoviesStream() {
        viewModelScope.launch {
            getFavouriteMoviesUseCase.execute().collect { favoriteMovies ->
                _uiState.update {
                    it.copy(
                        favoritesByMonth = groupMoviesByMonth(favoriteMovies),
                        isEmpty = favoriteMovies.isEmpty()
                    )
                }
            }
        }
    }

    fun onToggleFavorite(movieId: Long, currentIsFav: Boolean) {
        viewModelScope.launch {
            toggleFavoriteUseCase.execute(movieId, !currentIsFav)
        }
    }
}
