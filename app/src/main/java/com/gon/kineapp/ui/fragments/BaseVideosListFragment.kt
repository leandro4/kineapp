package com.gon.kineapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gon.kineapp.R
import com.gon.kineapp.model.Video
import com.gon.kineapp.ui.activities.ViewVideoActivity
import com.gon.kineapp.ui.adapters.PublicVideoAdapter
import com.gon.kineapp.utils.Constants
import com.gon.kineapp.utils.DialogUtil
import kotlinx.android.synthetic.main.fragment_public_videos_list.*

abstract class BaseVideosListFragment: BaseMvpFragment(), PublicVideoAdapter.VideoListener {

    private lateinit var adapter: PublicVideoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.gon.kineapp.R.layout.fragment_public_videos_list, container, false)
    }

    protected fun setVideos(videos: MutableList<Video>) {
        rvVideos.layoutManager = GridLayoutManager(context, 2)
        rvVideos.setHasFixedSize(true)
        adapter = PublicVideoAdapter(videos, this)
        adapter.removableVideos = removableVideos()
        rvVideos.adapter = adapter
    }

    override fun onRemoveVideoSelected(id: String) {
        DialogUtil.showOptionsAlertDialog(context!!, getString(R.string.remove_warning_title), getString(R.string.remove_video_warning_subtitle)) { removeVideo(id) }
    }

    override fun onVideoSelected(video: Video) {
        val intent = Intent(activity, ViewVideoActivity::class.java)
        intent.putExtra(Constants.VIDEO_EXTRA, video)
        activity?.startActivity(intent)
    }

    protected fun onVideoAdded(video: Video) {
        adapter.addVideo(video)
    }

    protected fun onRemovedVideo(id: String) {
        adapter.removeVideo(id)
    }

    protected open fun removableVideos(): Boolean {
        return true
    }

    protected abstract fun removeVideo(id: String)
}