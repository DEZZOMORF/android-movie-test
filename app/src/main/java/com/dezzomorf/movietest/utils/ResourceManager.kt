package com.dezzomorf.movietest.utils

import android.content.Context
import androidx.annotation.StringRes

class ResourceManager(private val context: Context) {

    fun getString(@StringRes id: Int): String = context.getString(id)
}
