package com.gon.kineapp.mvp.presenters

import com.gon.kineapp.api.CustomDisposableObserver
import com.gon.kineapp.api.KinesService
import com.gon.kineapp.model.responses.PatientListResponse
import com.gon.kineapp.mvp.views.PatientListView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PatientListPresenter: BasePresenter<PatientListView>() {

    fun getPatientList() {

        mvpView?.showProgressView()

        compositeSubscription!!.add(KinesService.getPatientList()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CustomDisposableObserver<PatientListResponse>() {
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

                override fun onNext(t: PatientListResponse) {
                    mvpView?.hideProgressView()
                    mvpView?.onPatientsReceived(t.patients)
                }
            }))
    }
}