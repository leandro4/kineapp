package com.gon.kineapp.ui.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.model.Video
import com.gon.kineapp.mvp.presenters.PublicVideosListPresenter
import com.gon.kineapp.mvp.views.PublicVideosListView
import com.gon.kineapp.ui.fragments.dialogs.InputDialogFragment
import com.gon.kineapp.utils.MyUser
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
        val videos = MyUser.get(context!!)?.medic?.videos
        videos?.let { setVideos(it) }

        fabAddVideo.setOnClickListener {
            Utils.takeVideo(activity!!, TAKE_VIDEO)
        }
    }

    override fun onVideoRemoved(id: String) {
        onRemovedVideo(id)
        val user = MyUser.get(context!!)?.apply { medic?.videos?.removeAll { it.id == id } }
        user?.let { MyUser.set(context!!, it) }
    }

    override fun onVideoUploaded(video: Video) {
        val user = MyUser.get(context!!)?.apply { medic?.videos?.add(video) }
        user?.let { MyUser.set(context!!, it) }
        onVideoAdded(video)
    }

    override fun startPresenter() {
        presenter.attachMvpView(this)
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
        if (requestCode == TAKE_VIDEO) {
            when (resultCode) {
                Activity.RESULT_OK -> data?.data?.let { compressVideo(it) }
                Activity.RESULT_CANCELED -> Toast.makeText(context, getString(R.string.video_cancelled), Toast.LENGTH_SHORT).show()
                else -> onErrorCode(getString(R.string.error_take_video))
            }
        }
    }

    fun onPermissionsGranted() {
        Utils.takeVideo(activity!!, TAKE_VIDEO)
    }

    private fun compressVideo(uri: Uri) {
        activity?.contentResolver?.let {
            showProgressView()
            Utils.compressVideo(uri, it, { msg ->
                hideProgressView()
                onErrorCode(msg) }, this::uploadVideo)
        }
    }

    private fun uploadVideo(path: String) {
        fragmentManager?.let {
            InputDialogFragment()
            .setTitle(getString(R.string.input_video_title))
            .setCancellable(false)
            .setCallback(object : InputDialogFragment.InputListener {
                override fun onInputDone(input: String) {
                    presenter.uploadVideo(path, input)
                }
            }).show(it, "inputDialog") }
    }
}