package com.gon.kineapp.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gon.kineapp.R
import com.gon.kineapp.model.Video
import com.gon.kineapp.utils.Constants
import kotlinx.android.synthetic.main.activity_view_video.*
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import com.gon.kineapp.api.UtilUrl

class ViewVideoActivity: AppCompatActivity() {

    private var video: Video? = null
    private var player: SimpleExoPlayer? = null
    private var currentWindows = 0
    private var playbackFrom = 0L
    private var playWhenReady = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_video)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        video = intent?.getParcelableExtra(Constants.VIDEO_EXTRA)

        supportActionBar?.title = video?.name
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
            DefaultRenderersFactory(this),
            DefaultTrackSelector(), DefaultLoadControl()
        )

        video_view.player = player

        player?.playWhenReady = playWhenReady
        player?.seekTo(currentWindows, playbackFrom)

        val mediaSource = buildMediaSource(Uri.parse(video?.url))
        player?.prepare(mediaSource, true, false)
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory("exoplayer-codelab")).createMediaSource(uri)
    }

    private fun releasePlayer() {
        if (player != null) {
            playbackFrom = player?.currentPosition!!
            currentWindows = player?.currentWindowIndex!!
            playWhenReady = player?.playWhenReady!!
            player?.release()
            player = null
        }
    }


    public override fun onStart() {
        super.onStart()
        if (SDK_INT > 23) initializePlayer()
    }

    public override fun onResume() {
        super.onResume()
        if (SDK_INT <= 23 || player == null) initializePlayer()
    }

    public override fun onPause() {
        super.onPause()
        if (SDK_INT <= 23) releasePlayer()
    }

    public override fun onStop() {
        super.onStop()
        if (SDK_INT > 23) releasePlayer()
    }
}