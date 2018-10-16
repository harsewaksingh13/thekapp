package com.harsewak.kapp.di

import android.support.v7.app.AppCompatActivity
import com.harsewak.kapp.ui.login.LoginActivityPresenter
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

/**
 * P is a presenter always inherited from Presenter
 * */
@Module
class ActivityModule(val activity: AppCompatActivity) {
    @Provides
    fun activity(): AppCompatActivity = activity
}

@ForActivity
@Component(modules = [ActivityModule::class], dependencies = [ApplicationComponent::class])
interface ActivityComponent {
    fun getLoginActivityPresenter(): LoginActivityPresenter
}

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class ForActivity