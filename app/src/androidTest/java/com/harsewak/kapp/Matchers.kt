package com.harsewak.kapp

import android.app.Activity
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*

class Matchers {


    fun <T : Activity> nextOpenActivityIs(clazz: Class<T>) {
        intended(IntentMatchers.hasComponent(clazz.name))
    }

    fun viewIsVisibleAndContainsText(@StringRes stringResource: Int) {
        onView(withText(stringResource))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    fun viewContainsText(@IdRes viewId: Int, @StringRes stringResource: Int) {
        Events.interactWith(viewId).check(matches(withText(stringResource)))
    }

    fun isDisplayed(viewId: Int) {
        Events.interactWith(viewId).check(matches(isDisplayed()))
    }
}