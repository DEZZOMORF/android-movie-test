package com.dezzomorf.movietest.di

import androidx.room.Room
import com.dezzomorf.movietest.data.local.AppDatabase
import com.dezzomorf.movietest.data.local.DATABASE_NAME
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    single {
        get<AppDatabase>().movieDao()
    }
}
