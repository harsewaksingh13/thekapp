package com.harsewak.kapp.base

import android.app.Activity
import com.harsewak.kapp.MyApplication
import com.harsewak.kapp.getApplication

fun Activity.application() : MyApplication = getApplication(applicationContext)

//var Activity.application : MyApplication = getApplication(applicationContext)