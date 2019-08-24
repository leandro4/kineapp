package com.gon.kineapp.mvp.presenters

import com.gon.kineapp.api.CustomDisposableObserver
import com.gon.kineapp.api.KinesService
import com.gon.kineapp.model.ExercisesCalendar
import com.gon.kineapp.model.Session
import com.gon.kineapp.model.responses.ExercisesResponse
import com.gon.kineapp.mvp.views.ExercisesView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ExercisesPresenter: BasePresenter<ExercisesView>() {

    fun getExercises() {
        mvpView?.showProgressView()

        compositeSubscription?.add(KinesService.getExercises()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CustomDisposableObserver<ExercisesResponse>() {
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

                override fun onNext(t: ExercisesResponse) {
                    mvpView?.hideProgressView()
                    mvpView?.onExercisesResponse(t.exercises)
                }
            }))
    }

    fun updateExercises(exercises: MutableList<ExercisesCalendar>) {

    }
}