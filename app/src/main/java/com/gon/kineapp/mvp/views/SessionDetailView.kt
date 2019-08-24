package com.gon.kineapp.mvp.views

import com.gon.kineapp.model.Photo

interface SessionDetailView: BaseView {

    fun onPhotoDeleted(id: String)
    fun onPhotoUploaded(photo: Photo)
    fun onSessionSaved()

}