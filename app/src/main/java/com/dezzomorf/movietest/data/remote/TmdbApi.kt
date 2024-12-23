package com.dezzomorf.movietest.data.remote

import com.dezzomorf.movietest.BuildConfig
import com.dezzomorf.movietest.data.remote.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("page") page: Int,
        @Query("vote_average.gte") voteAverageGte: Float = 7f,
        @Query("vote_count.gte") voteCountGte: Float = 100f,
        @Query("sort_by") sortBy: String = "primary_release_date.desc",
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
    ): MovieResponse
}
