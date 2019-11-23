package com.gon.kineapp.api

import android.util.Log
import io.reactivex.observers.DisposableObserver
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

abstract class CustomDisposableObserver<T> : DisposableObserver<T>() {

    private val TAG = CustomDisposableObserver::class.java.simpleName

    override fun onError(e: Throwable) {
        if (e is ConnectException || e is UnknownHostException) {
            onNoInternetConnection()

        } else if (e is HttpException) {
            Log.e(TAG, "-----------------------------------------------------------------------------------------------")
            Log.e(TAG, "Error: " + e.response().raw().toString())
            Log.e(TAG, "-----------------------------------------------------------------------------------------------")

            try {

                val message = if (e.response().code() != 500) e.response().errorBody()!!.string() else "Ups! Algo salió mal, vuelve a intentarlo!"
                onErrorCode(e.response().code(), message)

            } catch (e1: Exception) {
                onObserverError(e)
            }

        } else {
            onObserverError(e)
        }
    }

    override fun onComplete() {}

    abstract fun onNoInternetConnection()

    abstract fun onObserverError(e: Throwable)

    abstract fun onErrorCode(code: Int, message: String)
}