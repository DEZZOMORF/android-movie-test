package com.dezzomorf.movietest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dezzomorf.movietest.data.local.model.MovieEntity

const val DATABASE_VERSION = 1
const val DATABASE_NAME = "movie_test_db"

@Database(
    entities = [MovieEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
