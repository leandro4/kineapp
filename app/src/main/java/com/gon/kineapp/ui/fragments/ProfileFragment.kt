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
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.gon.kineapp.R
import com.gon.kineapp.mvp.presenters.LoginPresenter
import com.gon.kineapp.mvp.views.LoginView
import com.gon.kineapp.utils.ImageLoader
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.fragment_profile.*
import java.net.URI
import android.media.ExifInterface as ExifInterface1

class ProfileFragment: BaseMvpFragment(), LoginView {
    companion object {
        private const val REQUEST_TAKE_PHOTO = 0
        private const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1000
        private const val REQUEST_GALLERY_PERMISSION = 1010
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
        ImageLoader.load(this, getGoogleAccount()?.photoUrl).circle().into(civAvatar)
        nameTextView.text = getGoogleAccount()?.displayName
        tvSurname.text = getGoogleAccount()?.familyName
        emailTextView.text = getGoogleAccount()?.email
        this.setupClickListeners()
    }

    private fun setupClickListeners() {
        this.civAvatar.setOnClickListener {
            this.selectNewPhoto()
        }
        btnLogout.setOnClickListener {
            logOut()
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

    override fun startPresenter() {
        presenter.attachMvpView(this)
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
            val returnUri = data!!.data
            ImageLoader.load(this, returnUri).circle().into(civAvatar)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(grantResults.contains(PackageManager.PERMISSION_GRANTED) && requestCode == REQUEST_GALLERY_PERMISSION) {
            this.selectImageFromGallery()
        }
    }
}