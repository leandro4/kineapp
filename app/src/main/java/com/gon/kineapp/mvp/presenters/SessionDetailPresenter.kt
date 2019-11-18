package com.gon.kineapp.mvp.presenters

import com.gon.kineapp.api.CustomDisposableObserver
import com.gon.kineapp.api.KinesService
import com.gon.kineapp.model.Photo
import com.gon.kineapp.model.Session
import com.gon.kineapp.model.responses.PhotoResponse
import com.gon.kineapp.model.responses.PhotosListResponse
import com.gon.kineapp.mvp.views.SessionDetailView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers

class SessionDetailPresenter: BasePresenter<SessionDetailView>() {

    fun getPhoto(id: String) {
        mvpView?.showProgressView()

        compositeSubscription!!.add(
            KinesService.getPhoto(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CustomDisposableObserver<PhotoResponse>() {
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

                    override fun onNext(p: PhotoResponse) {
                        mvpView?.hideProgressView()
                        mvpView?.onPhotoLoaded(Photo(p.id, p.thumbnail, p.content, p.tag))
                    }
                })
        )
    }

    fun getPhotos(sessionId: String, photoSelected: String) {
        mvpView?.showProgressView()

        compositeSubscription?.addAll(KinesService.getPhotosBySession(sessionId)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CustomDisposableObserver<PhotosListResponse>() {
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

                override fun onNext(t: PhotosListResponse) {
                    mvpView?.hideProgressView()
                    val list = ArrayList<Photo>().apply { t.photos.forEach { add(it) } }
                    mvpView?.onPhotosReceived(list, photoSelected)
                }
            }))
    }

    fun deletePhoto(id: String) {
        mvpView?.showProgressView()

        compositeSubscription!!.add(
            KinesService.deletePhoto(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        mvpView?.hideProgressView()
                        mvpView?.onPhotoDeleted()
                    }

                    override fun onError(e: Throwable) {
                        mvpView?.hideProgressView()
                        mvpView?.onError(e)
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
                .subscribeWith(object : CustomDisposableObserver<PhotoResponse>() {
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

                    override fun onNext(p: PhotoResponse) {
                        mvpView?.hideProgressView()
                        mvpView?.onPhotoUploaded(Photo(p.id, p.thumbnail, p.content, p.tag))
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

    fun deleteSession(id: String) {
        mvpView?.showProgressView()

        compositeSubscription!!.add(
            KinesService.deleteSession(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        mvpView?.hideProgressView()
                        mvpView?.onSessionDeleted()
                    }

                    override fun onError(e: Throwable) {
                        mvpView?.hideProgressView()
                        mvpView?.onError(e)
                    }
                })
        )
    }
}