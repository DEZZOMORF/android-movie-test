package com.dezzomorf.movietest.presentation.ui

import androidx.lifecycle.ViewModel
import com.dezzomorf.movietest.R
import com.dezzomorf.movietest.domain.model.Movie
import com.dezzomorf.movietest.utils.ResourceManager
import com.dezzomorf.movietest.utils.TimeUtils

open class BaseViewModel(
    private val resourceManager: ResourceManager,
): ViewModel() {


    internal fun groupMoviesByMonth(movies: List<Movie>): Map<String, List<Movie>> {
        return movies.groupBy { movie ->
            if (movie.releaseDate != null) {
                TimeUtils.formatDateToMonthYear(movie.releaseDate)
            } else {
                resourceManager.getString(R.string.unknown_date)
            }
        }
    }
}