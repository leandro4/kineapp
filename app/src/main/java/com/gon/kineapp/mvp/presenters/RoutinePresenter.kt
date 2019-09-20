package com.gon.kineapp.mvp.presenters

import com.gon.kineapp.api.CustomDisposableObserver
import com.gon.kineapp.api.KinesService
import com.gon.kineapp.model.responses.ExercisesResponse
import com.gon.kineapp.mvp.views.RoutineView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RoutinePresenter: BasePresenter<RoutineView>() {

    fun createExercise(name: String, description: String, id: String?, day: Int) {
        mvpView?.showProgressView()

        compositeSubscription?.add(
            KinesService.createExercise(name, description, id, day)
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
                    mvpView?.onExerciseCreated()
                }
            }))
    }

    fun deleteExercise(id: String) {
        mvpView?.showProgressView()

        compositeSubscription?.add(
            KinesService.deleteExercise(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CustomDisposableObserver<Void>() {
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

                    override fun onNext(t: Void) {
                        mvpView?.hideProgressView()
                        mvpView?.onExerciseDeleted()
                    }
                }))
    }

    fun markAsDoneExercise(id: String) {
        mvpView?.showProgressView()

        compositeSubscription?.add(
            KinesService.deleteExercise(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CustomDisposableObserver<Void>() {
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

                    override fun onNext(t: Void) {
                        mvpView?.hideProgressView()
                        mvpView?.onExercisesEdited()
                    }
                }))
    }
}