package com.harsewak.kapp.ui.login

import com.harsewak.kapp.api.ServiceManager
import com.harsewak.kapp.api.response.LoginResponse
import com.harsewak.kapp.base.BasePresenter
import com.harsewak.kapp.base.Presenter
import com.harsewak.kapp.base.Utils.Companion.isNotValidEmail
import com.harsewak.kapp.base.Utils.Companion.isPasswordStrongEnough
import com.harsewak.kapp.base.View
import com.harsewak.kapp.data.DataManager
import com.harsewak.kapp.data.models.User
import io.reactivex.functions.Consumer
import javax.inject.Inject


class LoginActivityPresenter @Inject constructor(private val service: ServiceManager, private val dataManager: DataManager) : BasePresenter<LoginView>(), LoginPresenter {

    lateinit var email: String

    override fun onLoginClicked() {
        login(view.getEmail(), view.getPassword())
    }

    fun login(email: String, password: String) {
        this.email = email
        if (email.isEmpty()) {
            view.onEmptyEmail()
            return
        }
        if (isNotValidEmail(email)) {
            view.onInvalidEmail()
            return
        }
        if (password.isEmpty()) {
            view.onEmptyPassword()
            return
        }
        if(!isPasswordStrongEnough(password)){
            view.onWeakPassword()
            return
        }
        view.showProgress()
        executeApiRequest(service.login(email, password), Consumer { response -> onLoginResponse(response) })
    }

    private fun onLoginResponse(loginResponse: LoginResponse) {
        if (loginResponse.aux != null) {
            val userId = loginResponse.aux?.tokenPayload?.userId
            if (userId != null) {
                dataManager.saveToken(loginResponse.response().token)
                executeSingle(dataManager.users().save(User(userId, email)),
                        Consumer { id -> onUserSavedLocally(id) })
                return
            }
        }
        onDefaultError()
    }

    private fun onUserSavedLocally(id: Long) {
        if (id > 0) {
            view.onSuccessLogin()
        } else {
            onDefaultError()
        }
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