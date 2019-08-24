package com.gon.kineapp.mvp.presenters

import com.gon.kineapp.api.CustomDisposableObserver
import com.gon.kineapp.api.KinesService
import com.gon.kineapp.model.Photo
import com.gon.kineapp.model.Session
import com.gon.kineapp.mvp.views.SessionDetailView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SessionDetailPresenter: BasePresenter<SessionDetailView>() {

    fun getPhoto(id: String) {
        mvpView?.showProgressView()

        compositeSubscription!!.add(
            KinesService.deletePhoto(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CustomDisposableObserver<Photo>() {
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

                    override fun onNext(p: Photo) {
                        mvpView?.hideProgressView()
                        mvpView?.onPhotoDeleted(p.id)
                    }
                })
        )
    }

    fun deletePhoto(id: String) {
        mvpView?.showProgressView()

        compositeSubscription!!.add(
            KinesService.deletePhoto(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CustomDisposableObserver<Photo>() {
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

                    override fun onNext(p: Photo) {
                        mvpView?.hideProgressView()
                        mvpView?.onPhotoDeleted(p.id)
                    }
                })
        )
    }

    fun uploadPhoto(sessionId: String, contentPhoto: String, tag: String) {
        mvpView?.showProgressView()

        compositeSubscription!!.add(
            KinesService.uploadPhoto(sessionId, contentPhoto, tag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CustomDisposableObserver<Photo>() {
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

                    override fun onNext(p: Photo) {
                        mvpView?.onPhotoUploaded(p)
                        mvpView?.hideProgressView()
                    }
                })
        )
    }

    fun saveSession(session: Session) {
        mvpView?.showProgressView()

        compositeSubscription!!.add(
            KinesService.updateSession(session.id, session.description)
                .subscribeOn(Schedulers.io())
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

                    override fun onNext(t: Session) {
                        mvpView?.hideProgressView()
                        mvpView?.onSessionSaved()
                    }
                })
        )
    }
}