package com.gon.kineapp.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.model.Video
import com.gon.kineapp.mvp.presenters.PublicVideosListPresenter
import com.gon.kineapp.mvp.views.PublicVideosListView
import com.gon.kineapp.ui.adapters.PublicVideoAdapter
import com.gon.kineapp.utils.DialogUtil
import kotlinx.android.synthetic.main.fragment_public_videos_list.*

class PublicVideosListFragment : BaseMvpFragment(), PublicVideosListView, PublicVideoAdapter.VideoListener {

    private val presenter = PublicVideosListPresenter()
    private lateinit var adapter: PublicVideoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.gon.kineapp.R.layout.fragment_public_videos_list, container, false)
    }

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
        rvVideos.layoutManager = GridLayoutManager(context, 2)
        rvVideos.setHasFixedSize(true)
        adapter = PublicVideoAdapter(videos, this)
        rvVideos.adapter = adapter
    }

    override fun onVideoRemoved() {}

    override fun onVideoSelected(Video: Video) {
        Toast.makeText(context, "Ver video", Toast.LENGTH_SHORT).show()
    }

    override fun onRemoveVideo(id: String) {
        DialogUtil.showOptionsAlertDialog(context!!, getString(R.string.remove_warning_title), getString(R.string.remove_video_warning_subtitle)) {
            showProgressView()
            adapter.removeVideo(id)
            Handler().postDelayed({ hideProgressView() }, 1000)
        }
    }

    override fun startPresenter() {
        presenter.attachMvpView(this)
        presenter.getPublicVideosList()
    }

    override fun onDestroy() {
        presenter.detachMvpView()
        super.onDestroy()
    }

    override fun onErrorCode(message: String) {

    }
}