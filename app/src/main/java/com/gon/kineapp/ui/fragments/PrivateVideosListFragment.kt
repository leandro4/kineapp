package com.gon.kineapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.model.Patient
import com.gon.kineapp.model.Video
import com.gon.kineapp.mvp.presenters.PrivateVideosListPresenter
import com.gon.kineapp.mvp.presenters.PublicVideosListPresenter
import com.gon.kineapp.mvp.views.PrivateVideosView
import com.gon.kineapp.mvp.views.PublicVideosListView
import com.gon.kineapp.ui.activities.BaseActivity
import com.gon.kineapp.utils.Constants
import kotlinx.android.synthetic.main.fragment_public_videos_list.*

class PrivateVideosListFragment : BaseVideosListFragment(), PrivateVideosView {

    private val presenter = PrivateVideosListPresenter()
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
        setVideos(patient.videos)
    }

    override fun startPresenter() {
        presenter.attachMvpView(this)
    }

    override fun removeVideo(id: String) {
        presenter.removeVideo(id)
    }

    override fun onVideoRemoved(id: String) {
        onRemovedVideo(id)
        setVideosResultIntent() // after removed from adapter!
    }

    override fun onVideoUploaded(video: Video) {
        setVideosResultIntent()
    }

    override fun onDestroy() {
        presenter.detachMvpView()
        super.onDestroy()
    }

    private fun setVideosResultIntent() {
        val intent = Intent()
        val list = ArrayList<Video>()
        patient.videos.forEach { list.add(it) }
        intent.putParcelableArrayListExtra(Constants.VIDEO_EXTRA, list)
        activity?.setResult(Constants.EDITED_VIDEOS_CODE, intent)
    }
}