package com.gon.kineapp.mvp.presenters

import com.gon.kineapp.mvp.views.RoutineView

class RoutinePresenter: BasePresenter<RoutineView>() {

    fun markAsDone() {
        mvpView?.showProgressView()

        /*compositeSubscription?.add(KinesService.getExercises()
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
            }))*/
    }
}