package com.dezzomorf.movietest.di

import com.dezzomorf.movietest.utils.ResourceManager
import org.koin.dsl.module

val appModule = module {
    single { ResourceManager(get()) }
}
