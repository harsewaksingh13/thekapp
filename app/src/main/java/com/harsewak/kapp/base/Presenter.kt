package com.harsewak.kapp.base

import android.os.Bundle

interface Presenter {

    fun onCreate(view: View, savedInstanceState: Bundle?)
    fun onResume()
    fun onPause()

}

interface View {
    fun onError(error: String)
    fun <T> startActivity(clazz: Class<T>)
    fun showProgress()
    fun dismissProgress()
}