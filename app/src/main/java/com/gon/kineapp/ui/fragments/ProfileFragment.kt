package com.gon.kineapp.ui.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.app.ActivityCompat
import com.gon.kineapp.R
import com.gon.kineapp.model.User
import com.gon.kineapp.mvp.presenters.ProfilePresenter
import com.gon.kineapp.mvp.views.ProfileView
import com.gon.kineapp.ui.adapters.MedicSelectorAdapter
import com.gon.kineapp.ui.fragments.dialogs.SearchMedicFragment
import com.gon.kineapp.utils.ImageLoader
import com.gon.kineapp.utils.MyUser
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_profile.*
import android.media.ExifInterface as ExifInterface1

class ProfileFragment: BaseMvpFragment(), ProfileView, SearchMedicFragment.MedicListener {

    companion object {
        private const val REQUEST_TAKE_PHOTO = 0
        private const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1000
        private const val REQUEST_GALLERY_PERMISSION = 1010
    }

    var presenter = ProfilePresenter()

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
        MyUser.get(context!!).let {
            nameTextView.text = it!!.name
            tvSurname.text = it.surname
            if (it.isMedic()) {
                licenseTextView.text = it.medic!!.license
                rlMedic.visibility = View.GONE
            } else {
                rlLicense.visibility = View.GONE
                it.patient?.medicLicense?.let { license ->
                    medicAction.setImageResource(R.drawable.ic_remove)
                    tvMedic.text = "matrícula: " + license
                    medicAction.setOnClickListener { presenter.deleteCurrentMedic() }
                } ?: run  {
                    tvMedic.text = getString(R.string.not_medic_assigned)
                    medicAction.setOnClickListener { presenter.getMedicList() } }
            }
            emailTextView.text = it.mail
        }
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

    override fun onMedicSelected(license: String) {
        presenter.updateCurrentMedic(license)
    }

    override fun onMedicListResponse(medics: List<User>) {
        SearchMedicFragment.newInstance(medics, this).show(fragmentManager, "dialog")
    }

    override fun onMedicUpdated(user: User) {
        MyUser.set(context!!, user)
        initUI()
    }

    override fun onPhotoEdited() {

    }
}