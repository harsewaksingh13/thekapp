package com.harsewak.kapp.base

import android.os.Bundle
import android.support.annotation.VisibleForTesting
import com.harsewak.kapp.api.ApiError
import com.harsewak.kapp.api.Response
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.jetbrains.annotations.TestOnly

@Suppress("UNCHECKED_CAST")
open class BasePresenter<V : View> : Presenter {


    lateinit var view: V

    lateinit var disposables: CompositeDisposable

    //we create this while testing only
    @VisibleForTesting
    var isTesting: Boolean = false


    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        this.view = view as V
        disposables = CompositeDisposable()
    }

    override fun onResume() {

    }

    override fun onPause() {
        disposables.dispose()
    }

    fun <T : Response> executeApiRequest(observable: Observable<T>, consumer: Consumer<T>) {
        disposables.add(subscribe(composeAndFilter(observable), consumer, Consumer { throwable -> onException(throwable) }))
    }

//    fun <T : Response> executeApiRequest(observable: Observable<T>) {
//        disposables.add(subscribe(composeAndFilter(observable), Consumer { response -> onResponse(response) }, Consumer(::onException)))
//    }
//
//    protected open fun onResponse(response: Response) {
//
//    }

    /**
     * a common compose scheduler for api calls, this also filter the response on basis of response
     */
    private fun <T : Response> composeAndFilter(observable: Observable<T>): Observable<T> {
        if (isTesting) {
            return composeAndFilter(observable, trampoline())
        }
        return composeAndFilter(observable, schedulers())
    }

    /**
     * a common compose scheduler for api calls, this also filter the response on basis of response
     */
    fun <T : Response> composeAndFilter(observable: Observable<T>, transformer: ObservableTransformer<T, T>): Observable<T> {
        return observable.compose(transformer).filter { response -> validResponse(response) }
    }


    protected fun <T> schedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer { it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
    }

    @TestOnly
    protected fun <T> trampoline(): ObservableTransformer<T, T> {
        return ObservableTransformer { it.subscribeOn(Schedulers.trampoline()).observeOn(Schedulers.trampoline()) }
    }


    @TestOnly
    protected fun <T> singleTrampoline(): SingleTransformer<T, T> {
        return SingleTransformer { it.subscribeOn(Schedulers.trampoline()).observeOn(Schedulers.trampoline()) }
    }

    /*
        transformation from newThread to mainThread
     */
    fun <T> singleTransformation(): SingleTransformer<T, T> {
        if (isTesting)
            return singleTrampoline()
        return SingleTransformer {
            it.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
        }
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

    /**
     * subscribe with disposable
     */
    protected fun <T> subscribe(observable: Observable<T>, consumer: Consumer<T>, onError: Consumer<Throwable>): Disposable {
        return observable.subscribe(consumer, onError)
    }

    /**
     * subscribe with disposable
     */
    protected fun <T> subscribeSingle(observable: Single<T>, consumer: Consumer<T>, onError: Consumer<Throwable>): Disposable {
        return observable.subscribe(consumer, onError)
    }

    fun <T> executeSingle(observable: Single<T>, consumer: Consumer<T>) {
        disposables.add(subscribeSingle(observable.compose(singleTransformation()), consumer, Consumer { throwable -> onException(throwable) }))
    }

    protected fun onException(throwable: Throwable) {
        onError(Error(throwable))
    }

    protected fun onError(error: Error) {
        view.onError(error.localizedMessage)
    }

    fun onErrorText(error: String) {
        onError(Error(error))
    }

    /**
     * a default error helper function
     * **/
    fun onDefaultError() {
        onErrorText(ApiError.defaultError())
    }
}
