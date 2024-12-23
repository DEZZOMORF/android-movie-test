package com.dezzomorf.movietest.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dezzomorf.movietest.BuildConfig
import com.dezzomorf.movietest.domain.model.Movie
import com.dezzomorf.movietest.utils.TimeUtils

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("poster_path")
    val posterPath: String,
    @ColumnInfo("release_date")
    val releaseDate: String,
    @ColumnInfo("vote_average")
    val voteAverage: Double,
    @ColumnInfo("overview")
    val overview: String,
    @ColumnInfo("is_favorite")
    val isFavorite: Boolean
) {

    fun mapToDomain() = Movie(
        id = id,
        title = title,
        posterPath = BuildConfig.POSTER_HOST + posterPath,
        releaseDate = TimeUtils.parseToDate(releaseDate),
        voteAverage = voteAverage,
        overview = overview,
        isFavorite = isFavorite
    )
}
