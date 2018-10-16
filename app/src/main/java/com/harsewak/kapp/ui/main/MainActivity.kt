package com.harsewak.kapp.ui.main

import android.os.Bundle
import com.harsewak.kapp.R
import com.harsewak.kapp.base.BaseActivity
import com.harsewak.kapp.di.ActivityComponent

class MainActivity : BaseActivity<MainActivityPresenter>() {

    override fun presenter(component: ActivityComponent): MainActivityPresenter {
        return MainActivityPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
