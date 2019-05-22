package com.gon.kineapp.mvp.presenters

interface Presenter<MvpView> {

    fun attachMvpView(view: MvpView)

    fun dettachMvpView()
}