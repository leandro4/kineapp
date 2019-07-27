package com.gon.kineapp.mvp.presenters

import com.gon.kineapp.api.CustomDisposableObserver
import com.gon.kineapp.api.KinesService
import com.gon.kineapp.model.responses.PatientListResponse
import com.gon.kineapp.model.responses.PublicVideosListResponse
import com.gon.kineapp.mvp.views.PatientListView
import com.gon.kineapp.mvp.views.PublicVideosListView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PublicVideosListPresenter: BasePresenter<PublicVideosListView>() {

    fun getPublicVideosList() {

        mvpView?.showProgressView()

        compositeSubscription!!.add(KinesService.getPublicVideos()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CustomDisposableObserver<PublicVideosListResponse>() {
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

                override fun onNext(t: PublicVideosListResponse) {
                    mvpView?.hideProgressView()
                    mvpView?.onPublicVideosReceived(t.publicVideos)
                }
            }))
    }
}