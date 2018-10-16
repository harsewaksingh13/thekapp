package com.harsewak.kapp.api.request

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response


data class LoginRequest(val email: String, val password: String)

class HeaderInterceptor(private val token: String?) : Interceptor {

    private val clientId = "9xsEQDt9RjgYeWB"

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val headers = Headers.Builder()
                .add("client-id", clientId)
                .add("token", token)
                .build()
        request = request.newBuilder().headers(headers).build()
        return chain.proceed(request)
    }
}