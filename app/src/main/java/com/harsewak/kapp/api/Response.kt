package com.harsewak.kapp.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

interface Response {
    fun getError(): Error?
}

interface ResponseObject<T> : Response {
    fun getResponse(): T
}

interface ResponseArray<T> : Response {
    fun getResponse(): List<T>
}

class ApiError : Error() {
    @SerializedName("text")
    @Expose
    var error: String? = null

    @SerializedName("code")
    @Expose
    var code: String? = null

    override fun getLocalizedMessage(): String {
        return if (error != null)
            error as String
        else
            defaultError()
    }

    companion object {
        fun defaultError(): String = "Something went wrong!!"
    }
}

open class BasicResponse : Response {

    @SerializedName("error")
    @Expose
    var error: ApiError? = null

    override fun getError(): Error? {
        return error
    }
}

open class DefaultResponse<T> : BasicResponse(), ResponseArray<T> {

    @SerializedName("data")
    @Expose
    var data: List<T>? = null


    override fun getResponse(): List<T> {
        if (data != null)
            return data as List<T>
        return ArrayList()
    }

    fun response(): T {
        return getResponse()[0]
    }

}

open class DefaultResponseObject<T> : BasicResponse(), ResponseObject<T> {

    @SerializedName("data")
    @Expose
    var data: T? = null


    override fun getResponse(): T {
        return data!!
    }

}