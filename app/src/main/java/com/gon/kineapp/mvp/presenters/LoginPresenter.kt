package com.gon.kineapp.mvp.presenters

import com.gon.kineapp.api.CustomDisposableObserver
import com.gon.kineapp.api.KinesService
import com.gon.kineapp.model.responses.LoginResponse
import com.gon.kineapp.mvp.views.LoginView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginPresenter: BasePresenter<LoginView>() {

    fun requestLogin() {

        mvpView?.showProgressView()

        compositeSubscription!!.add(KinesService.requestLogin()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CustomDisposableObserver<LoginResponse>() {
                override fun onNoInternetConnection() {
                    mvpView?.onNoInternetConnection()
                }

                override fun onObserverError(e: Throwable) {
                    mvpView?.onError(e)
                }

                override fun onErrorCode(code: Int, message: String) {
                    mvpView?.onErrorCode(message)
                }

                override fun onNext(t: LoginResponse) {
                    mvpView?.onLoginSuccess()
                }
            })
        )
    }

}