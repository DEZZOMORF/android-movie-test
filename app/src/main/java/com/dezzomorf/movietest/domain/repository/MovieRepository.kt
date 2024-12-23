package com.dezzomorf.movietest.domain.repository

import com.dezzomorf.movietest.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMoviesStream(): Flow<List<Movie>>
    fun getFavoriteMoviesStream(): Flow<List<Movie>>
    suspend fun fetchMovies(page: Int): Result<Unit>
    suspend fun toggleFavorite(movieId: Long, isFavorite: Boolean)
}
