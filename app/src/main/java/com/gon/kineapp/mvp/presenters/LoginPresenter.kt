package com.gon.kineapp.mvp.presenters

import com.gon.kineapp.api.CustomDisposableObserver
import com.gon.kineapp.api.KinesService
import com.gon.kineapp.model.responses.LoginResponse
import com.gon.kineapp.model.responses.UserExistsResponse
import com.gon.kineapp.mvp.views.LoginView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginPresenter: BasePresenter<LoginView>() {

    fun requestLogin() {

        mvpView?.showProgressView()

/*        compositeSubscription!!.add(KinesService.registerUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CustomDisposableObserver<LoginResponse>() {
                override fun onNoInternetConnection() {
                    mvpView?.hideProgressView()
                    mvpView?.onNoInternetConnection()
                }

                override fun onObserverError(e: Throwable) {
                    mvpView?.hideProgressView()
                    mvpView?.onError(e)
                }

                override fun onErrorCode(code: Int, message: String) {
                    mvpView?.hideProgressView()
                    mvpView?.onErrorCode(message)
                }

                override fun onNext(t: LoginResponse) {
                    mvpView?.hideProgressView()
                    mvpView?.onLoginSuccess()
                }
            })
        )*/
    }

    fun userExists(googleToken: String) {

        mvpView?.showProgressView()

        compositeSubscription!!.add(KinesService.userExists(googleToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CustomDisposableObserver<UserExistsResponse>() {
                override fun onNoInternetConnection() {
                    mvpView?.hideProgressView()
                    mvpView?.onNoInternetConnection()
                }

                override fun onObserverError(e: Throwable) {
                    mvpView?.hideProgressView()
                    mvpView?.onError(e)
                }

                override fun onErrorCode(code: Int, message: String) {
                    mvpView?.hideProgressView()
                    if (code == 406) {
                        mvpView?.onUserDoesntExists()
                    }
                }

                override fun onNext(t: UserExistsResponse) {
                    mvpView?.hideProgressView()
                    mvpView?.onUserRetrieved(t.myUser, t.questions)
                }
            })
        )
    }
}