package com.gon.kineapp.mvp.presenters

import com.gon.kineapp.api.CustomDisposableObserver
import com.gon.kineapp.api.KinesService
import com.gon.kineapp.model.Question
import com.gon.kineapp.model.responses.LoginResponse
import com.gon.kineapp.model.responses.UserExistsResponse
import com.gon.kineapp.model.responses.UserRegisteredResponse
import com.gon.kineapp.mvp.views.LoginView
import com.gon.kineapp.utils.Authorization
import com.gon.kineapp.utils.QuestionsList
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginPresenter: BasePresenter<LoginView>() {

    fun registerUser(token: String, firstName: String, lastName: String, license: String?, number: String?, birthday: String?, email: String, questionId: Int, answer: String) {

        mvpView?.showProgressView()

        compositeSubscription!!.add(KinesService.registerUser(token, firstName, lastName, license, number, birthday, email, questionId, answer)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CustomDisposableObserver<UserRegisteredResponse>() {
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

                override fun onNext(t: UserRegisteredResponse) {
                    mvpView?.hideProgressView()
                    Authorization.getInstance().set(t.token)
                    mvpView?.onUserCreated(t.myUser)
                }
            })
        )
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
                        val questions = Gson().fromJson<QuestionsList.Questions>(message, QuestionsList.Questions::class.java)
                        mvpView?.onUserDoesntExists(questions.questions)
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