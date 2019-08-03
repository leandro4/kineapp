package com.gon.kineapp.mvp.presenters

import com.gon.kineapp.api.CustomDisposableObserver
import com.gon.kineapp.api.KinesService
import com.gon.kineapp.model.responses.LoginResponse
import com.gon.kineapp.mvp.views.QuestionView
import com.gon.kineapp.utils.Authorization
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class QuestionPresenter: BasePresenter<QuestionView>() {

    fun checkAnswer(googleToken: String, questionId: Int, answer: String) {

        mvpView?.showProgressView()

        compositeSubscription!!.add(KinesService.checkAnswer(questionId, answer, googleToken)
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
                    when (code) {
                        401 -> mvpView?.onAttemptsLimit()
                        406 -> mvpView?.onAnswerInvalid()
                        else -> mvpView?.onErrorCode(message)
                    }
                }

                override fun onNext(t: LoginResponse) {
                    mvpView?.let {
                        Authorization.getInstance().set(t.token)
                        it.onCheckedAnswer()
                    }
                }
            })
        )

    }

}