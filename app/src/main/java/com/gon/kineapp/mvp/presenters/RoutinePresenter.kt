package com.gon.kineapp.mvp.presenters

import android.content.Context
import com.gon.kineapp.api.CustomDisposableObserver
import com.gon.kineapp.api.KinesService
import com.gon.kineapp.model.Exercise
import com.gon.kineapp.model.User
import com.gon.kineapp.model.responses.ExercisesResponse
import com.gon.kineapp.mvp.views.RoutineView
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers

class RoutinePresenter: BasePresenter<RoutineView>() {

    fun syncCurrentUser(context: Context) {

        compositeSubscription!!.add(KinesService.syncCurrentUser(false, context)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CustomDisposableObserver<User>() {
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

                override fun onNext(t: User) {
                    mvpView?.hideProgressView()
                    mvpView?.onUserLoaded()
                }
            }))
    }

    fun createExercise(patientId: String, name: String, description: String, id: String?, day: ArrayList<Int>) {
        mvpView?.showProgressView()

        compositeSubscription?.add(
            KinesService.createExercise(patientId, name, description, id, day)
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
                    mvpView?.onExercisesCreated(t.exercises)
                }
            }))
    }

    fun deleteExercise(id: String) {
        mvpView?.showProgressView()

        compositeSubscription?.add(
            KinesService.deleteExercise(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableCompletableObserver() {
                    public override fun onStart() {}
                    override fun onError(error: Throwable) {
                        mvpView?.hideProgressView()
                        mvpView?.onError(error)
                    }
                    override fun onComplete() {
                        mvpView?.hideProgressView()
                        mvpView?.onExerciseDeleted()
                    }
                }))
    }

    fun markAsDoneExercise(id: String) {
        mvpView?.showProgressView()

        compositeSubscription?.add(
            KinesService.markAsDoneExercise(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CustomDisposableObserver<Exercise>() {
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

                    override fun onNext(t: Exercise) {
                        mvpView?.hideProgressView()
                        mvpView?.onExercisesEdited(t)
                    }
                }))
    }
}