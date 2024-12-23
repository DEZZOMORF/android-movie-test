package com.dezzomorf.movietest.domain.usecase

import com.dezzomorf.movietest.domain.repository.MovieRepository

class FetchMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend fun fetchMovies(page: Int) = repository.fetchMovies(page = page)
}
