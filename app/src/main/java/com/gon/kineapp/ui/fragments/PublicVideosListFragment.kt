package com.gon.kineapp.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.gon.kineapp.model.Video
import com.gon.kineapp.mvp.presenters.PublicVideosListPresenter
import com.gon.kineapp.mvp.views.PublicVideosListView
import kotlinx.android.synthetic.main.fragment_public_videos_list.*

class PublicVideosListFragment : BaseVideosListFragment(), PublicVideosListView {

    private val presenter = PublicVideosListPresenter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        fabAddVideo.setOnClickListener {
            Toast.makeText(context, "agregar video", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPublicVideosReceived(videos: MutableList<Video>) {
        setVideos(videos)
    }

    override fun onVideoRemoved(id: String) {
        onRemovedVideo(id)
    }

    override fun startPresenter() {
        presenter.attachMvpView(this)
        presenter.getPublicVideosList()
    }

    override fun removeVideo(id: String) {
        presenter.removeVideo(id)
    }

    override fun onDestroy() {
        presenter.detachMvpView()
        super.onDestroy()
    }
}