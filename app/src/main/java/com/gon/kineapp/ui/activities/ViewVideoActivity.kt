package com.gon.kineapp.ui.activities

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gon.kineapp.R
import com.gon.kineapp.model.Video
import com.gon.kineapp.utils.Constants
import kotlinx.android.synthetic.main.activity_view_video.*
import android.widget.MediaController

class ViewVideoActivity: AppCompatActivity() {

    private var video: Video? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_video)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = getString(com.gon.kineapp.R.string.public_video_view_title)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        video = intent?.getParcelableExtra(Constants.VIDEO_EXTRA) as Video
        video?.let {
            val uri = Uri.parse(it.videoUrl)
            vvPlayer.setVideoURI(uri)

            val vidControl = MediaController(this)
            vidControl.setAnchorView(vvPlayer)
            vvPlayer.setMediaController(vidControl)
            vvPlayer.start()

            tvTitle.text = it.title
        }
    }
}