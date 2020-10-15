package com.harsewak.kapp.base

import android.os.Bundle
import com.harsewak.kapp.api.ApiError
import com.harsewak.kapp.api.Response


open class BasePresenter<V : View> : Presenter {


    var view: V? = null

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        this.view = view as? V
    }

    override fun onResume() {

    }

    override fun onPause() {

    }



    private fun validResponse(response: Response?): Boolean {
        if (response != null) {
            val error = response.getError()
            if (error != null) {
                onError(error)
                return false
            }
            return true
        }
        return false
    }


    open fun onError(throwable: Throwable) {
        onError(Error(throwable))
    }

    open fun onError(error: Error) {
        view?.onError(error.localizedMessage)
    }

    open fun onErrorText(error: String) {
        onError(Error(error))
    }

    /**
     * a default error helper function
     * **/
    fun onDefaultError() {
        onErrorText(ApiError.defaultError())
    }
}
