package com.gon.kineapp.mvp.views

import com.gon.kineapp.model.Photo
import com.gon.kineapp.model.responses.PhotoResponse

interface SessionDetailView: BaseView {

    fun onPhotoDeleted(photo: Photo)
    fun onPhotoUploaded(photo: Photo)
    fun onPhotoLoaded(photo: Photo)
    fun onSessionSaved()

}