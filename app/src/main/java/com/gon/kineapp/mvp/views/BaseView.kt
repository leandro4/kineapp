package com.gon.kineapp.mvp.views

import android.content.Context

interface BaseView {

    fun getMvpContext(): Context?

    fun onError(var1: Throwable)

    fun onNoInternetConnection()

    fun onErrorCode(message: String)

    fun showProgressView()

    fun hideProgressView()
}