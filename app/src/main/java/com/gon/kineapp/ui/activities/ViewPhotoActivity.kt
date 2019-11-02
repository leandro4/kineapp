package com.gon.kineapp.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.gon.kineapp.R
import com.gon.kineapp.utils.Constants
import com.gon.kineapp.model.Photo
import com.gon.kineapp.model.PhotoTag
import com.gon.kineapp.utils.DialogUtil
import com.gon.kineapp.utils.ImageLoader
import com.gon.kineapp.utils.Utils
import kotlinx.android.synthetic.main.activity_view_photo.*

class ViewPhotoActivity: AppCompatActivity() {

    private var photo: Photo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_view_photo)

        setSupportActionBar(toolbar)

        photo = intent?.getParcelableExtra(Constants.PHOTO_EXTRA)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        photo?.let {
            supportActionBar?.title = PhotoTag.valueOf(it.tag).getCompleteName()
            ImageLoader.load(this, Utils.convertImage(it.content!!)).into(ivPhoto)
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
            intent.putExtra(Constants.PHOTO_EXTRA, photo?.id)
            setResult(Constants.REMOVED_PHOTO_CODE, intent)
            onBackPressed()
        }
    }
}