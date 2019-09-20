package com.gon.kineapp.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.gon.kineapp.R
import com.gon.kineapp.utils.Constants
import kotlinx.android.synthetic.main.activity_create_exercise.*

class CreateExerciseActivity : LockableActivity() {

    private var day = 0

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
        val adapter = ArrayAdapter.createFromResource(this, R.array.days_array, R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(R.layout.spinner_text_arrow_white)
        spDay.adapter = adapter
        spDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                day = position
            }
        }

        fabSave.setOnClickListener {
            save()
        }
    }

    private fun save() {
        if (etTitle.text.toString().isEmpty()) {
            etTitle.error = getString(R.string.mandatory_field)
            return
        } else if (etDescription.text.toString().isEmpty()) {
            etDescription.error = getString(R.string.mandatory_field)
            return
        }

        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(Constants.EXERCISE_TITLE_EXTRA, etTitle.text.toString())
            putExtra(Constants.EXERCISE_DESCRIPTION_EXTRA, etDescription.text.toString())
            putExtra(Constants.EXERCISE_DAY_EXTRA, day)
            //putExtra(Constants.EXERCISE_VIDEO_ID_EXTRA, null)
        })
        finish()
    }
}
