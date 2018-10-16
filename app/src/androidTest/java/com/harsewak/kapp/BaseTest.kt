package com.harsewak.kapp

import android.app.Activity
import android.content.Intent
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
abstract class BaseTest<T : Activity>(clazz: Class<T>) {

    @Rule
    @JvmField
    val testRule: ActivityTestRule<T> = IntentsTestRule(clazz)


    fun launchActivity() {
        testRule.launchActivity(Intent())
    }

}