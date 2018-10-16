package com.harsewak.kapp

import android.app.Activity
import kotlin.reflect.KClass

open class Robot {
    private val matchers: Matchers = Matchers()

    private val events: Events = Events()

    fun click(viewId: Int) {
        events.clickOnView(viewId)
    }

    fun typeText(viewId: Int, text: String) {
        events.typeInView(viewId, text)
    }

    fun <T : Activity> isNextActivity(kClass: KClass<T>) {
        matchers.nextOpenActivityIs(kClass.java)
    }

    fun isViewDisplayed(viewId: Int) {
        matchers.isDisplayed(viewId)
    }
}