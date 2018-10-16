package com.harsewak.kapp.base

import android.support.annotation.StringRes
import android.widget.EditText

fun EditText.getString(): String = text.toString().trim()

fun EditText.setError(@StringRes stringRes: Int) {
    error = context.getString(stringRes)
}

class Utils {
    companion object {
        fun isNotValidEmail(input: String): Boolean = !android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()

        fun isPasswordStrongEnough(input: String): Boolean = input.length >= 6
    }
}
