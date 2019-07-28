package com.gon.kineapp.mvp.presenters

import android.os.Handler
import com.gon.kineapp.api.CustomDisposableObserver
import com.gon.kineapp.api.KinesService
import com.gon.kineapp.model.responses.PatientListResponse
import com.gon.kineapp.model.responses.PublicVideosListResponse
import com.gon.kineapp.mvp.views.PatientListView
import com.gon.kineapp.mvp.views.PrivateVideosView
import com.gon.kineapp.mvp.views.PublicVideosListView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PrivateVideosListPresenter: BasePresenter<PrivateVideosView>() {

    fun removeVideo(id: String) {

        mvpView?.showProgressView()

        Handler().postDelayed(
            {
                mvpView?.hideProgressView()
                mvpView?.onVideoRemoved(id)
            }, 1000)
    }

    fun uploadVideo() {

    }
}