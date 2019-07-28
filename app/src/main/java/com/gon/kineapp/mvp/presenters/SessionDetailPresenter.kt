package com.gon.kineapp.mvp.presenters

import android.os.Handler
import com.gon.kineapp.model.Session
import com.gon.kineapp.mvp.views.SessionDetailView

class SessionDetailPresenter: BasePresenter<SessionDetailView>() {

    fun deletePhoto(id: String) {
        mvpView?.showProgressView()
        Handler().postDelayed( {
            mvpView?.onPhotoDeleted(id)
            mvpView?.hideProgressView() }, 1000)
    }

    fun saveSession(session: Session) {
        mvpView?.showProgressView()
        Handler().postDelayed({
            mvpView?.onSessionSaved()
            mvpView?.hideProgressView()
        }, 1000)
    }
}