package com.dezzomorf.movietest.domain.usecase

import com.dezzomorf.movietest.domain.model.Movie
import com.dezzomorf.movietest.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMoviesUseCase(
    private val repository: MovieRepository
) {
    fun execute(): Flow<List<Movie>> = repository.getMoviesStream()
}
