package com.harsewak.kapp.data

import com.harsewak.kapp.api.*
import com.harsewak.kapp.api.request.LoginRequest
import com.harsewak.kapp.api.response.LoginResponse
import com.harsewak.kapp.data.models.User
import javax.inject.Inject


class BaseInteractor {

}

class AuthInteractor @Inject constructor(private val service: ServiceManager, private val dataManager: DataManager) {


    fun login(email: String, password: String, responseHandler: ResponseHandler<User>, errorHandler: ErrorHandler) {
        service.auth.login(LoginRequest(email, password)).execute({
            onLoginResponse(email, it,responseHandler, errorHandler)
        }, {
            errorHandler(it)
        })
    }


    private fun onLoginResponse(email: String,loginResponse: LoginResponse,responseHandler: ResponseHandler<User>, errorHandler: ErrorHandler) {
        if (loginResponse.aux != null) {
            val userId = loginResponse.aux?.tokenPayload?.userId
            if (userId != null) {
                dataManager.saveToken(loginResponse.response().token)
                val user = User(userId, email)
                dataManager.users().save(user).execute({
                    responseHandler(user)
                }, { errorHandler(it) })
                return
            }
        }
        errorHandler(ApiError())
    }

}