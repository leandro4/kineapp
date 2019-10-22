package com.gon.kineapp.mvp.presenters

import android.content.Context
import com.gon.kineapp.api.CustomDisposableObserver
import com.gon.kineapp.api.KinesService
import com.gon.kineapp.model.Patient
import com.gon.kineapp.model.User
import com.gon.kineapp.model.responses.PatientListResponse
import com.gon.kineapp.mvp.views.PatientListView
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File

class PatientListPresenter: BasePresenter<PatientListView>() {

    fun syncCurrentUser(context: Context) {
        compositeSubscription!!.add(KinesService.syncCurrentUser(true, context)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CustomDisposableObserver<User>() {
                override fun onNoInternetConnection() {}
                override fun onObserverError(e: Throwable) {}
                override fun onErrorCode(code: Int, message: String) {}

                override fun onNext(t: User) {}
            }))
    }

    fun getPatientList() {

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
                    t.readOnlyPatients.forEach { it.patient?.readOnly = true }
                    mvpView?.onPatientsReceived(t.patients, t.readOnlyPatients)
                }
            }))
    }
}