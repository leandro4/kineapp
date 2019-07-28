package com.gon.kineapp.mvp.views

import com.gon.kineapp.model.Video

interface PrivateVideosView: BaseView {

    fun onVideoRemoved(id: String)
    fun onVideoUploaded(video: Video)

}