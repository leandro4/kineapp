package com.gon.kineapp.mvp.presenters

import android.os.Handler
import com.gon.kineapp.api.CustomDisposableObserver
import com.gon.kineapp.api.KinesService
import com.gon.kineapp.model.Photo
import com.gon.kineapp.model.Session
import com.gon.kineapp.model.responses.SessionListResponse
import com.gon.kineapp.mvp.views.SessionListView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList

class SessionListPresenter: BasePresenter<SessionListView>() {

    fun getSessions() {
        mvpView?.showProgressView()

        compositeSubscription?.addAll(KinesService.getSessionList()
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
                    mvpView?.onSessionsReceived(t.patients)
                }
            }))
    }

    fun createSession(patientId: String) {
        mvpView?.showProgressView()

        Handler().postDelayed( {
            mvpView?.hideProgressView()
            val session = Session("98891", patientId, "21/08/2019", "Obs", ArrayList<Photo>().toMutableList())
            mvpView?.onSessionCreated(session)
        }, 1000)

    }

}