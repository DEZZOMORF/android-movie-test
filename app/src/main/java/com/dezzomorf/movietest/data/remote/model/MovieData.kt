package com.dezzomorf.movietest.data.remote.model

import com.dezzomorf.movietest.data.local.model.MovieEntity
import com.dezzomorf.movietest.utils.TimeUtils
import com.google.gson.annotations.SerializedName

data class MovieData(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("overview")
    val overview: String?,
) {
    fun mapToEntity() = MovieEntity(
        id = id,
        title = title.orEmpty(),
        posterPath = posterPath.orEmpty(),
        releaseDate = releaseDate.orEmpty(),
        voteAverage = voteAverage ?: 0.0,
        overview = overview.orEmpty(),
        isFavorite = false,
    )
}
