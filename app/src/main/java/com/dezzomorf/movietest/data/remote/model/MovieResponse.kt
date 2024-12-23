package com.dezzomorf.movietest.data.remote.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<MovieData>,
    @SerializedName("total_pages")
    val totalPages: Int
)
