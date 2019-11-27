package com.gon.kineapp.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import com.gon.kineapp.R
import com.gon.kineapp.model.Photo
import com.gon.kineapp.model.PhotoTag
import com.gon.kineapp.ui.adapters.FragmentPagerAdapter
import com.gon.kineapp.ui.fragments.FragmentPhoto
import com.gon.kineapp.utils.*
import kotlinx.android.synthetic.main.activity_view_photo.*

class ViewPhotoActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_view_photo)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initUI()
    }

    private fun initUI() {
        val adapter = FragmentPagerAdapter(supportFragmentManager)
        vpPhotos.adapter = adapter
        PhotosRepository.photos.forEach { adapter.addPage(FragmentPhoto(it.content!!)) }
        vpIndicator.setViewPager(vpPhotos)

        val selectedId = intent.getStringExtra(Constants.PHOTO_EXTRA)
        vpPhotos.currentItem = PhotosRepository.photos.indexOfFirst { it.id == selectedId }
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
            val photo = PhotosRepository.photos[vpPhotos.currentItem]
            intent.putExtra(Constants.PHOTO_EXTRA, photo.id)
            setResult(Constants.REMOVED_PHOTO_CODE, intent)
            onBackPressed()
        }
    }
}