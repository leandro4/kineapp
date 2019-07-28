package com.gon.kineapp.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.model.Patient
import com.gon.kineapp.model.Video
import com.gon.kineapp.mvp.presenters.PublicVideosListPresenter
import com.gon.kineapp.mvp.views.PublicVideosListView
import com.gon.kineapp.ui.activities.BaseActivity
import kotlinx.android.synthetic.main.fragment_public_videos_list.*

class PrivateVideosListFragment : BaseVideosListFragment(), PublicVideosListView {

    private val presenter = PublicVideosListPresenter()
    private lateinit var patient: Patient

    companion object {
        fun newInstance(patient: Patient): PrivateVideosListFragment {
            val frag = PrivateVideosListFragment()
            frag.patient = patient
            return frag
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as BaseActivity).setToolbarTitle(String.format(getString(R.string.videos_from_title), patient.name))
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