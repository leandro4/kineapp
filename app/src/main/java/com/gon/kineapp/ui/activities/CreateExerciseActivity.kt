package com.gon.kineapp.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.gon.kineapp.R
import com.gon.kineapp.model.Video
import com.gon.kineapp.ui.adapters.VideoSelectorAdapter
import com.gon.kineapp.ui.fragments.dialogs.SelectVideoFragment
import com.gon.kineapp.utils.Constants
import com.gon.kineapp.utils.DialogUtil
import com.gon.kineapp.utils.ImageLoader
import com.gon.kineapp.utils.MyUser
import kotlinx.android.synthetic.main.activity_create_exercise.*

class CreateExerciseActivity : LockableActivity(), SelectVideoFragment.VideoSelectorListener {

    private lateinit var days: List<View>
    private var videoSelected: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_exercise)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.new_exercise_title)

        initUI()
    }

    private fun initUI() {
        fabSave.setOnClickListener {
            save()
        }
        days = listOf(day0, day1, day2, day3, day4, day5, day6).toList()
        days.forEach { it.setOnClickListener { b -> b.isSelected = !b.isSelected } }

        ivDelete.setOnClickListener { removeVideoSelected() }
        rvSelectVideo.setOnClickListener { selectVideo() }
    }

    private fun save() {
        if (etTitle.text.toString().isEmpty()) {
            etTitle.error = getString(R.string.mandatory_field)
            return
        } else if (etDescription.text.toString().isEmpty()) {
            etDescription.error = getString(R.string.mandatory_field)
            return
        } else if (days.firstOrNull { it.isSelected } == null ) {
            DialogUtil.showGenericAlertDialog(this, getString(R.string.new_exercise_warning_title), getString(R.string.new_exercise_warning_message))
            return
        }

        val list = ArrayList<Int>().apply { days.forEachIndexed { index, view -> if (view.isSelected) add(index) } }

        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(Constants.EXERCISE_TITLE_EXTRA, etTitle.text.toString())
            putExtra(Constants.EXERCISE_DESCRIPTION_EXTRA, etDescription.text.toString())
            putIntegerArrayListExtra(Constants.EXERCISE_DAYS_EXTRA, list)
            videoSelected?.let { putExtra(Constants.EXERCISE_VIDEO_ID_EXTRA, it) }
        })
        finish()
    }

    private fun selectVideo() {
        MyUser.get(this)?.medic?.videos?.let {
            if (it.isEmpty()) {
                DialogUtil.showGenericAlertDialog(this, getString(R.string.no_videos_available_title), getString(R.string.no_videos_available_message))
            } else {
                SelectVideoFragment().setListener(this).show(supportFragmentManager, "selectVideo")
            }
        }
    }

    override fun onVideoSelected(video: Video) {
        rvSelectVideo.setOnClickListener(null)
        videoSelected = video.id
        tvName.text = video.name
        ivDelete.visibility = View.VISIBLE
        cvImage.visibility = View.VISIBLE
        ImageLoader.load(this, video.thumbUrl).into(ivThumb)
    }

    private fun removeVideoSelected() {
        rvSelectVideo.setOnClickListener { selectVideo() }
        videoSelected = null
        tvName.text = getString(R.string.video_not_selected)
        ivDelete.visibility = View.GONE
        cvImage.visibility = View.GONE
        ivThumb.setImageResource(0)
    }
}
