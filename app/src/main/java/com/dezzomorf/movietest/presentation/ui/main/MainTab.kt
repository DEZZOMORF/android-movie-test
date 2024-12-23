package com.dezzomorf.movietest.presentation.ui.main

import androidx.annotation.StringRes
import com.dezzomorf.movietest.R

enum class MainTab(
    @StringRes
    val title: Int
) {
    FILMS(R.string.main_tab_films),
    FAVOURITE(R.string.main_tab_favourites);
}