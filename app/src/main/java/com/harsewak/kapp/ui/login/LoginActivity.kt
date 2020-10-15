package com.harsewak.kapp.ui.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.harsewak.kapp.R
import com.harsewak.kapp.base.BaseActivity
import com.harsewak.kapp.base.getString
import com.harsewak.kapp.base.setError
import com.harsewak.kapp.di.ActivityComponent
import com.harsewak.kapp.ui.main.MainActivity


class LoginActivity :BaseActivity<LoginActivityPresenter>(), LoginView {

    private lateinit var email: EditText
    private lateinit var password : EditText

    override fun presenter(component: ActivityComponent): LoginActivityPresenter {
        return component.getLoginActivityPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        email = findViewById(R.id.emailEditText)
        password = findViewById(R.id.passwordEditText)
        findViewById<Button>(R.id.buttonLogin).setOnClickListener {presenter?.onLoginClicked()}
    }

    override fun getEmail(): String {
        return "john.smith@gmail.com"//email.getString()
    }

    override fun getPassword(): String {
        return "123456"//password.getString()
    }

    override fun onSuccessLogin() {
        startActivity(MainActivity::class.java)
    }

    override fun onEmptyEmail() {
        email.setError(R.string.error_empty_email)
    }

    override fun onEmptyPassword() {
        password.setError(R.string.error_empty_email)
    }

    override fun onInvalidEmail() {
        email.setError(R.string.error_invalid_email)
    }

    override fun onWeakPassword() {
        password.setError(R.string.error_weak_password)
    }
}

