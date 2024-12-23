package com.dezzomorf.movietest

import android.app.Application
import com.dezzomorf.movietest.di.appModule
import com.dezzomorf.movietest.di.databaseModule
import com.dezzomorf.movietest.di.networkModule
import com.dezzomorf.movietest.di.repositoryModule
import com.dezzomorf.movietest.di.viewModelModule
import com.dezzomorf.movietest.di.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MovieTestApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MovieTestApp)
            modules(
                listOf(
                    networkModule,
                    databaseModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule,
                    appModule
                )
            )
        }
    }
}
