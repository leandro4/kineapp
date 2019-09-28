package com.gon.kineapp.ui.activities

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.gon.kineapp.R
import com.gon.kineapp.utils.Constants
import com.gon.kineapp.model.Photo
import com.gon.kineapp.utils.PhotosRepository
import com.gon.kineapp.utils.Utils
import com.gonanimationlib.animations.Animate
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_time_line.*
import java.util.concurrent.TimeUnit

class TimeLineActivity: LockableActivity() {

    private var TIME_BETWEEN_PICS = 5L

    private var bitmaps = ArrayList<Bitmap>()
    private lateinit var imageA: ImageView
    private lateinit var imageB: ImageView
    private var switched = false
    private var actualPic = 0
    private var runing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_time_line)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.timeline_title)

        initUI()
    }

    private fun switchContainers() {
        if (switched) {
            imageA = findViewById(R.id.ivPhotoFirst)
            imageB = findViewById(R.id.ivPhotoSecond)
        } else {
            imageB = findViewById(R.id.ivPhotoFirst)
            imageA = findViewById(R.id.ivPhotoSecond)
        }
        switched = !switched
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initUI() {
        imageA = findViewById(R.id.ivPhotoFirst)
        imageB = findViewById(R.id.ivPhotoSecond)

        PhotosRepository.photos.forEach { it.content?.let { t -> bitmaps.add(Utils.convertImage(t)) } }

        fabController.setOnClickListener {
            if (runing) {
                runing = !runing
                stop()
            } else {
                runing = !runing
                start()
            }
        }
        ivLowSpeed.setOnClickListener {
            if (TIME_BETWEEN_PICS > 1) {
                TIME_BETWEEN_PICS--
            }
            updateTextSpeed()
        }
        ivHighSpeed.setOnClickListener {
            if (TIME_BETWEEN_PICS < 5) {
                TIME_BETWEEN_PICS++
            }
            updateTextSpeed()
        }

        updateTextSpeed()
    }

    private fun updateTextSpeed() {
        tvSpeed.text = String.format("%d", TIME_BETWEEN_PICS)
    }

    @SuppressLint("CheckResult")
    private fun start() {
        if (!runing) return
        fabController.setImageResource(R.drawable.ic_stop)
        imageA.setImageBitmap(bitmaps[actualPic])
        Animate.ALPHA(1f).duration(500).startAnimation(imageA)
        Animate.ALPHA(0f).duration(500).startAnimation(imageB)
        switchContainers()
        actualPic++

        if (actualPic == bitmaps.size) {
            actualPic = 0
            runing = false
            stop()
        } else {
            Observable.just(true).delay(getTime(), TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { start() }
        }
    }

    private fun stop() {
        fabController.setImageResource(R.drawable.ic_play)
    }

    private fun getTime(): Long {
        return when(TIME_BETWEEN_PICS) {
            1L -> 10L
            2L -> 6L
            3L -> 4L
            4L -> 2L
            else -> 1L
        }
    }
}