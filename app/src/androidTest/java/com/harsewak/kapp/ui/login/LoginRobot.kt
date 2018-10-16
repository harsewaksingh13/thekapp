package com.harsewak.kapp.ui.login

import com.harsewak.kapp.R
import com.harsewak.kapp.Robot
import com.harsewak.kapp.ui.main.MainActivity


class LoginRobot : Robot() {


    companion object {
        val LOGIN = R.id.buttonLogin
        val EMAIL = R.id.emailEditText
        val PASSWORD = R.id.passwordEditText
    }

    fun email(email: String): LoginRobot {
        typeText(EMAIL, email)
        return this
    }

    fun password(password: String): LoginRobot {
        typeText(PASSWORD, password)
        return this
    }

    fun submit(): LoginRobot {
//        onView(withId(LOGIN)).perform(click())
        click(LOGIN)
        return this
    }

    fun isSuccessful() {
        isNextActivity(MainActivity::class)
    }

    fun isFailed() {

    }
}