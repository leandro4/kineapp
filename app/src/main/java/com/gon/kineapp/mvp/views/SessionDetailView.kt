package com.gon.kineapp.mvp.views

interface SessionDetailView: BaseView {

    fun onPhotoDeleted(id: String)
    fun onSessionSaved()

}