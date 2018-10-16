package com.harsewak.kapp.ui

import android.os.Bundle
import android.os.Handler
import com.harsewak.kapp.R
import com.harsewak.kapp.base.BaseActivity
import com.harsewak.kapp.base.BasePresenter
import com.harsewak.kapp.base.View
import com.harsewak.kapp.di.ActivityComponent
import com.harsewak.kapp.ui.login.LoginActivity

class SplashActivity : BaseActivity<BasePresenter<View>>() {

    override fun presenter(component: ActivityComponent): BasePresenter<View> {
        return BasePresenter()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({startActivity(LoginActivity::class.java)},3000)
    }
}