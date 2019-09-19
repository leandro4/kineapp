package com.gon.kineapp.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.gon.kineapp.model.Video
import com.gon.kineapp.mvp.presenters.PublicVideosListPresenter
import com.gon.kineapp.mvp.views.PublicVideosListView
import com.gon.kineapp.utils.Utils
import kotlinx.android.synthetic.main.fragment_public_videos_list.*

class PublicVideosListFragment : BaseVideosListFragment(), PublicVideosListView {

    private val presenter = PublicVideosListPresenter()

    companion object {
        const val TAKE_VIDEO = 1000
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        fabAddVideo.setOnClickListener {
            Utils.takeVideo(activity!!, TAKE_VIDEO)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PatientDetailFragment.TAKE_VIDEO) {
            when (resultCode) {
                Activity.RESULT_OK -> Toast.makeText(context, "Video saved to:\n" + data?.data, Toast.LENGTH_LONG).show()
                Activity.RESULT_CANCELED -> Toast.makeText(context, "Video recording cancelled.", Toast.LENGTH_LONG).show()
                else -> Toast.makeText(context, "Failed to record video", Toast.LENGTH_LONG).show()
            }
        }
    }
}