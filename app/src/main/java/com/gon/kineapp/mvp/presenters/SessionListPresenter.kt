package com.gon.kineapp.mvp.presenters

import com.gon.kineapp.api.CustomDisposableObserver
import com.gon.kineapp.api.KinesService
import com.gon.kineapp.model.Session
import com.gon.kineapp.model.responses.SessionListResponse
import com.gon.kineapp.mvp.views.SessionListView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SessionListPresenter: BasePresenter<SessionListView>() {

    fun getSessions(id: String) {
        mvpView?.showProgressView()

        compositeSubscription?.addAll(KinesService.getSessionList(id)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CustomDisposableObserver<SessionListResponse>() {
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

                override fun onNext(t: SessionListResponse) {
                    mvpView?.hideProgressView()
                    mvpView?.onSessionsReceived(t.sessions)
                }
            }))
    }

    fun createSession(patientId: String) {
        mvpView?.showProgressView()

        compositeSubscription?.addAll(KinesService.createSession(patientId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CustomDisposableObserver<Session>() {
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

                override fun onNext(session: Session) {
                    mvpView?.hideProgressView()
                    mvpView?.onSessionCreated(session)
                }
            }))
    }

}