package com.gon.kineapp.mvp.presenters

import android.os.Handler
import com.gon.kineapp.api.CustomDisposableObserver
import com.gon.kineapp.api.KinesService
import com.gon.kineapp.model.Video
import com.gon.kineapp.model.responses.PatientListResponse
import com.gon.kineapp.model.responses.PublicVideosListResponse
import com.gon.kineapp.mvp.views.PatientListView
import com.gon.kineapp.mvp.views.PublicVideosListView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers

class PublicVideosListPresenter: BasePresenter<PublicVideosListView>() {

    fun removeVideo(id: String) {

        mvpView?.showProgressView()

        compositeSubscription!!.add(
            KinesService.deleteVideo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    override fun onComplete() {
                        mvpView?.onVideoRemoved(id)
                    }

                    override fun onError(e: Throwable) {
                        mvpView?.hideProgressView()
                        mvpView?.onError(e)
                    }
                })
        )
    }

    fun uploadVideo(path: String, name: String) {

        mvpView?.showProgressView()

        compositeSubscription?.addAll(KinesService.uploadVideo(path, name)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CustomDisposableObserver<Video>() {
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

                override fun onNext(t: Video) {
                    mvpView?.hideProgressView()
                    mvpView?.onVideoUploaded(t)
                }
            }))
    }
}