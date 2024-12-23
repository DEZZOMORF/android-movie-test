package com.dezzomorf.movietest.data.repository

import com.dezzomorf.movietest.data.local.MovieDao
import com.dezzomorf.movietest.data.remote.TmdbApi
import com.dezzomorf.movietest.domain.model.Movie
import com.dezzomorf.movietest.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl(
    private val movieDao: MovieDao,
    private val tmdbApi: TmdbApi
) : MovieRepository {

    override fun getMoviesStream(): Flow<List<Movie>> {
        return movieDao.getAllMovies().map { entities ->
            entities.map { it.mapToDomain() }
        }
    }

    override fun getFavoriteMoviesStream(): Flow<List<Movie>> {
        return movieDao.getFavoriteMovies().map { entities ->
            entities.map { it.mapToDomain() }
        }
    }

    override suspend fun fetchMovies(page: Int): Result<Unit> {
        return try {
            val response = tmdbApi.discoverMovies(page = page)
            val entities = response.results.map { it.mapToEntity() }
            movieDao.insertMoviesWithFavoritesPreserved(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleFavorite(movieId: Long, isFavorite: Boolean) {
        movieDao.updateFavorite(movieId, isFavorite)
    }
}
