package com.gon.kineapp.mvp.presenters

import com.gon.kineapp.mvp.views.BaseView
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<T: BaseView> : Presenter<T> {

    protected var mvpView: T? = null
    protected var compositeSubscription: CompositeDisposable? = null

    override fun attachMvpView(view: T) {
        this.mvpView = view
        this.compositeSubscription = CompositeDisposable()
    }

    override fun detachMvpView() {
        this.mvpView = null
        compositeSubscription?.let {
            it.clear()
        }
    }
}