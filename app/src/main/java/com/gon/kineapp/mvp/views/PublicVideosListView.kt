package com.gon.kineapp.mvp.views

import com.gon.kineapp.model.Video

interface PublicVideosListView: BaseView {

    fun onPublicVideosReceived(videos: MutableList<Video>)

    fun onVideoRemoved()

}