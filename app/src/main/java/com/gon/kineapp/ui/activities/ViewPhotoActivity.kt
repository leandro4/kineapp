package com.gon.kineapp.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.gon.kineapp.R
import com.gon.kineapp.utils.Constants
import com.gon.kineapp.model.Photo
import com.gon.kineapp.utils.DialogUtil
import com.gon.kineapp.utils.ImageLoader
import com.gon.kineapp.utils.Utils
import kotlinx.android.synthetic.main.activity_view_photo.*
import kotlinx.android.synthetic.main.fragment_session_detail.*

class ViewPhotoActivity: AppCompatActivity() {

    private var photo: Photo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_photo)

        setSupportActionBar(toolbar)

        photo = intent?.getParcelableExtra(Constants.PHOTO_EXTRA)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = photo?.tag

        photo?.let {
            ImageLoader.load(this, Uri.parse(it.imgUrl)).into(ivPhoto)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_photo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.remove -> {
                removePhoto()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun removePhoto() {
        DialogUtil.showOptionsAlertDialog(this, getString(R.string.remove_warning_title), getString(R.string.remove_pic_warning_subtitle)) {
            val intent = Intent()
            intent.putExtra(Constants.PHOTO_EXTRA, photo)
            setResult(Constants.REMOVE_PHOTO, intent)
            onBackPressed()
        }
    }
}