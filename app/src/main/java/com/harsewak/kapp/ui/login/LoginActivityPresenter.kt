package com.harsewak.kapp.ui.login

import com.harsewak.kapp.base.BasePresenter
import com.harsewak.kapp.base.Presenter
import com.harsewak.kapp.base.Utils.Companion.isNotValidEmail
import com.harsewak.kapp.base.Utils.Companion.isPasswordStrongEnough
import com.harsewak.kapp.base.View
import com.harsewak.kapp.data.AuthInteractor
import javax.inject.Inject


class LoginActivityPresenter @Inject constructor(private val authInteractor: AuthInteractor) : BasePresenter<LoginView>(), LoginPresenter {


    override fun onLoginClicked() {
        login(view?.getEmail() ?: "", view?.getPassword() ?: "")
    }

    fun login(email: String, password: String) {
        if (email.isEmpty()) {
            view?.onEmptyEmail()
            return
        }
        if (isNotValidEmail(email)) {
            view?.onInvalidEmail()
            return
        }
        if (password.isEmpty()) {
            view?.onEmptyPassword()
            return
        }
        if (!isPasswordStrongEnough(password)) {
            view?.onWeakPassword()
            return
        }
        view?.showProgress()

        authInteractor.login(email, password, {
            view?.dismissProgress()
            view?.onSuccessLogin()
        }, {
            onError(it)
        })
    }
}

interface LoginPresenter : Presenter {
    fun onLoginClicked()
}

interface LoginView : View {
    fun onSuccessLogin()
    fun getEmail(): String
    fun getPassword(): String
    fun onEmptyEmail()
    fun onEmptyPassword()
    fun onInvalidEmail()
    fun onWeakPassword()
}