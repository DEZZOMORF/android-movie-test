package com.dezzomorf.movietest.domain.model

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import java.util.Date

data class Movie(
    val id: Long,
    val title: String,
    val posterPath: String,
    val overview: String,
    val releaseDate: Date?,
    val voteAverage: Double,
    val isFavorite: Boolean
) {
    companion object {
        val mock = Movie(
            id = Long.MAX_VALUE,
            title = LoremIpsum(2).values.joinToString(),
            posterPath = "",
            releaseDate = Date(),
            overview = LoremIpsum(15).values.joinToString(),
            voteAverage = 4.6,
            isFavorite = true
        )
    }
}
