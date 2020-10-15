package com.harsewak.kapp.api

import com.harsewak.kapp.api.request.LoginRequest
import com.harsewak.kapp.api.response.LoginResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST


typealias ResponseHandler<T> = (T) -> Unit

typealias ErrorHandler = (Throwable) -> Unit

class RequestHandler(private val disposable: Disposable) {
    fun cancel() {
        disposable.dispose()
    }
}

typealias RequestExecutor<T> = Observable<T>

fun <T : Response> RequestExecutor<T>.execute(responseHandler: ResponseHandler<T>, errorHandler: ErrorHandler): RequestHandler {
    return RequestHandler(compose {
        it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }.filter {
        it.getError() == null
    }.subscribe({
        responseHandler(it)
    }, {
        errorHandler(it)
    }))
}


interface AuthService {
    @POST("/tokens")
    fun login(@Body loginRequest: LoginRequest): RequestExecutor<LoginResponse>
}

interface ServiceManager {
    val auth: AuthService
}

class ServiceManagerImpl(private val retrofit: Retrofit) : ServiceManager {


    override val auth: AuthService
        get() = retrofit.create(AuthService::class.java)

}