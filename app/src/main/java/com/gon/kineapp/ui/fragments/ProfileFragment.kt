package com.gon.kineapp.ui.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.app.ActivityCompat
import com.bumptech.glide.Glide
import com.gon.kineapp.R
import com.gon.kineapp.mvp.presenters.LoginPresenter
import com.gon.kineapp.mvp.views.LoginView
import kotlinx.android.synthetic.main.fragment_profile.*
import android.media.ExifInterface as ExifInterface1

class ProfileFragment: BaseMvpFragment(), LoginView {
    companion object {
        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1000
        private val REQUEST_GALLERY_PERMISSION = 1010
    }

    var presenter = LoginPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startPresenter()
        initUI()
    }

    private fun initUI() {
        Glide.with(context!!).load(Uri.parse("https://www.elpopular.pe/sites/default/files/styles/img_620x465/public/imagen/2018/10/21/Noticia-218698-hombre-se-llevo-sorpresa-al-descubrir-que-su-perrito-era-un-animal-de-otra-especie.jpg?itok=3a9Eb3oL"))
            .into(civAvatar)
        this.setupClickListeners()
    }

    private fun setupClickListeners() {
        this.civAvatar.setOnClickListener {
            this.selectNewPhoto()
        }
    }

    private fun selectNewPhoto() {
        if(!this.galleryPermissionsGranted()) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_GALLERY_PERMISSION);
        } else {
            this.selectImageFromGallery()
        }
    }

    private fun galleryPermissionsGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(this.context!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(this.activity!!.packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
    }
/*
    private fun takePhoto() {
        val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent1.resolveActivity(this.activity!!.packageManager) != null) {
            startActivityForResult(intent1, REQUEST_TAKE_PHOTO)
        }
    }
*/
    override fun startPresenter() {
        presenter.attachMvpView(this)
    }

    override fun onErrorCode(message: String) {
        showErrorMessage(getString(R.string.generic_error_message))
    }

    override fun onDestroy() {
        presenter.detachMvpView()
        super.onDestroy()
    }

    override fun onLoginSuccess() {
    }

    override fun onLoginFailure() {
        showErrorMessage(getString(R.string.generic_error_message))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM) {
            val returnUri = data!!.getData()
            Glide.with(this).load(returnUri).into(civAvatar)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(grantResults.contains(PackageManager.PERMISSION_GRANTED) && requestCode == REQUEST_GALLERY_PERMISSION) {
            this.selectImageFromGallery()
        }
    }
}