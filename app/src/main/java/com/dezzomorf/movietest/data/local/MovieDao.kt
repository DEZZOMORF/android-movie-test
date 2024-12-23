package com.dezzomorf.movietest.data.local

import androidx.room.*
import com.dezzomorf.movietest.data.local.model.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies WHERE id IN (:ids)")
    suspend fun getMoviesByIds(ids: List<Long>): List<MovieEntity>

    @Query("SELECT * FROM movies ORDER BY release_date DESC")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE is_favorite = 1 ORDER BY release_date DESC")
    fun getFavoriteMovies(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoviesWithFavoritesPreserved(newMovies: List<MovieEntity>) {
        val existingMovies = getMoviesByIds(newMovies.map { it.id })
        val mergedMovies = newMovies.map { newMovie ->
            val oldMovie = existingMovies.firstOrNull { it.id == newMovie.id }
            if (oldMovie != null) {
                newMovie.copy(isFavorite = oldMovie.isFavorite)
            } else {
                newMovie
            }
        }
        insertMovies(mergedMovies)
    }

    @Query("UPDATE movies SET is_favorite = :isFavorite WHERE id = :movieId")
    suspend fun updateFavorite(movieId: Long, isFavorite: Boolean)
}
