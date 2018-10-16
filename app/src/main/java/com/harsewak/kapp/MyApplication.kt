package com.harsewak.kapp

import android.app.Application
import android.content.Context
import com.harsewak.kapp.di.ApplicationComponent
import com.harsewak.kapp.di.ApplicationModule
import com.harsewak.kapp.di.DaggerApplicationComponent


class MyApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
    }
}

fun getApplication(context: Context): MyApplication {
    return context.applicationContext as MyApplication
}