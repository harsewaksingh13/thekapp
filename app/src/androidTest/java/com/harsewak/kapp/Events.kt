package com.harsewak.kapp

import android.support.annotation.IdRes
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.matcher.ViewMatchers.withId

class Events {

    companion object {
        fun interactWith(@IdRes viewId: Int): ViewInteraction = onView(withId(viewId))
    }

    fun clickOnView(@IdRes viewId: Int) {
        interactWith(viewId).perform(click())
    }

    fun typeInView(@IdRes viewId: Int, text: String) {
        interactWith(viewId).perform(typeText(text))
    }
}