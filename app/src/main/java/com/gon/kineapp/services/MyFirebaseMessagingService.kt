package com.gon.kineapp.services

import android.annotation.SuppressLint
import com.gon.kineapp.api.KinesService
import com.google.firebase.messaging.FirebaseMessagingService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }

    @SuppressLint("CheckResult")
    private fun sendRegistrationToServer(firebaseId: String) {
        KinesService.updateFirebaseId(firebaseId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableCompletableObserver() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
            })
    }

}