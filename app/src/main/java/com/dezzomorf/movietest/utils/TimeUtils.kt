package com.dezzomorf.movietest.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtils {

    fun parseToDate(text: String): Date? {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            sdf.parse(text)
        } catch (e: ParseException) {
            null
        }
    }

    fun formatDateToMonthYear(date: Date): String {
        val formatter = SimpleDateFormat("MMM yyyy", Locale.getDefault())
        return formatter.format(date)
    }
}
