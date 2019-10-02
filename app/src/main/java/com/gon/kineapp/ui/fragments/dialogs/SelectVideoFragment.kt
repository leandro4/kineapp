package com.gon.kineapp.ui.fragments.dialogs

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gon.kineapp.R
import com.gon.kineapp.model.User
import com.gon.kineapp.model.Video
import com.gon.kineapp.ui.adapters.MedicSelectorAdapter
import com.gon.kineapp.ui.adapters.VideoSelectorAdapter
import com.gon.kineapp.utils.MyUser
import kotlinx.android.synthetic.main.fragment_video_selector.*

class SelectVideoFragment: BaseDialogFragment(), VideoSelectorAdapter.VideoListener {

    private var callback: VideoSelectorListener? = null
    private lateinit var adatper: VideoSelectorAdapter

    interface VideoSelectorListener {
        fun onVideoSelected(video: Video)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adatper = VideoSelectorAdapter(MyUser.get(context!!)?.medic?.videos!!, this)
        rvVideos.layoutManager = LinearLayoutManager(context)
        rvVideos.setHasFixedSize(true)
        rvVideos.adapter = adatper

        flContainer.setOnClickListener { dismiss() }
    }

    override fun onVideoSelected(video: Video) {
        callback?.onVideoSelected(video)
        dismiss()
    }

    fun setListener(listener: VideoSelectorListener): SelectVideoFragment {
        callback = listener
        return this
    }
}