package com.gon.kineapp.mvp.presenters

import com.gon.kineapp.api.CustomDisposableObserver
import com.gon.kineapp.api.KinesService
import com.gon.kineapp.model.SharedMedic
import com.gon.kineapp.model.User
import com.gon.kineapp.model.responses.MedicListResponse
import com.gon.kineapp.mvp.views.ProfileView
import com.gon.kineapp.utils.MyUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProfilePresenter : BasePresenter<ProfileView>() {

    fun getMedicList() {

        mvpView?.showProgressView()

        compositeSubscription!!.add(KinesService.getMedicList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CustomDisposableObserver<MedicListResponse>() {
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

                override fun onNext(t: MedicListResponse) {
                    mvpView?.hideProgressView()
                    mvpView?.onMedicListResponse(t.medics)
                }
            })
        )
    }

    fun updateUserThumbnail(picture: String) {

        mvpView?.showProgressView()

        compositeSubscription!!.add(KinesService.updateUserThumbnail(picture)
            .subscribeOn(Schedulers.io())
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
                }
            })
        )
    }

    fun updateCurrentMedic(sharedMedic: SharedMedic) {

        mvpView?.showProgressView()

        compositeSubscription!!.add(KinesService.updateCurrentMedic(sharedMedic)
            .subscribeOn(Schedulers.io())
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
                    mvpView?.onMedicUpdated(t)
                }
            })
        )
    }

    fun deleteCurrentMedic() {
        updateCurrentMedic(SharedMedic("0", "", ""))
    }

}