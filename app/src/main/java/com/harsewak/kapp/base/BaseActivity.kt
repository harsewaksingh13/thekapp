package com.harsewak.kapp.base

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.harsewak.kapp.di.ActivityComponent
import com.harsewak.kapp.di.ActivityModule
import com.harsewak.kapp.di.ApplicationComponent
import com.harsewak.kapp.di.DaggerActivityComponent
import com.harsewak.kapp.getApplication

@SuppressLint("Registered")
abstract class BaseActivity<P : Presenter> : AppCompatActivity(), View {

    var presenter: P? = null
    private lateinit var component: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .applicationComponent(applicationComponent())
                .build()
        presenter = presenter(component)
        presenter?.onCreate(this, savedInstanceState)
    }

    abstract fun presenter(component: ActivityComponent): P


    private fun applicationComponent(): ApplicationComponent {
        return application().applicationComponent
    }

    override fun onResume() {
        super.onResume()
        presenter?.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter?.onPause()
    }

    override fun onError(error: String) {
        Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
    }

    override fun <T> startActivity(clazz: Class<T>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }

    override fun showProgress() {

    }

    override fun dismissProgress() {

    }
}