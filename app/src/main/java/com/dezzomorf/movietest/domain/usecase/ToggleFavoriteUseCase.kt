package com.dezzomorf.movietest.domain.usecase

import com.dezzomorf.movietest.domain.repository.MovieRepository

class ToggleFavoriteUseCase(
    private val repository: MovieRepository
) {
    suspend fun execute(movieId: Long, isFavorite: Boolean) {
        repository.toggleFavorite(movieId, isFavorite)
    }
}
