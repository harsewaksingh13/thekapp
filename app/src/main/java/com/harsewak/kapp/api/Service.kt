package com.harsewak.kapp.api

import com.harsewak.kapp.api.request.LoginRequest
import com.harsewak.kapp.api.response.LoginResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface Service {
    @POST("/tokens")
    fun login(@Body loginRequest: LoginRequest) : Observable<LoginResponse>
}

interface ServiceManager {
    fun login(email: String, password: String): Observable<LoginResponse>
}

class ServiceManagerImpl(private val service: Service) : ServiceManager {


    override fun login(email: String, password: String): Observable<LoginResponse> {
        return service.login(LoginRequest(email,password))
    }
}