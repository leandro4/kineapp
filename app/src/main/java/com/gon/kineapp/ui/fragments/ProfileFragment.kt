package com.gon.kineapp.ui.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gon.kineapp.R
import com.gon.kineapp.model.SharedMedic
import com.gon.kineapp.model.User
import com.gon.kineapp.mvp.presenters.ProfilePresenter
import com.gon.kineapp.mvp.views.ProfileView
import com.gon.kineapp.ui.adapters.MedicSelectorAdapter
import com.gon.kineapp.ui.adapters.SharedMedicAdapter
import com.gon.kineapp.ui.fragments.dialogs.SearchMedicFragment
import com.gon.kineapp.utils.ImageLoader
import com.gon.kineapp.utils.MyUser
import com.gon.kineapp.utils.Utils
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_profile.*
import android.media.ExifInterface as ExifInterface1

class ProfileFragment: BaseMvpFragment(), ProfileView, SearchMedicFragment.MedicListener,
    SharedMedicAdapter.SharedMedicListener {

    companion object {
        private const val REQUEST_TAKE_PHOTO = 0
        private const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1000
        private const val REQUEST_GALLERY_PERMISSION = 1010
    }

    var presenter = ProfilePresenter()
    private lateinit var adapter: SharedMedicAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startPresenter()
        initUI()
    }

    private fun initUI() {
        MyUser.get(context!!).let {
            it?.thumbnail?.let { base64 ->
                if (!base64.isEmpty()) ImageLoader.load(context, Utils.convertImage(base64)).circle().into(civAvatar)
            }
            nameTextView.text = it!!.name
            tvSurname.text = it.surname
            if (it.isMedic()) {
                licenseTextView.text = it.medic!!.license
                rlMedic.visibility = View.GONE
                rlSharedMedics.visibility = View.GONE
            } else {
                rlLicense.visibility = View.GONE
                it.patient?.currentMedic?.let { medic ->
                    medicAction.setImageResource(R.drawable.ic_remove)
                    tvMedic.text = String.format("%s %s", medic.name, medic.surname)
                    medicAction.setOnClickListener { presenter.deleteCurrentMedic() }
                } ?: run  {
                    tvMedic.text = getString(R.string.not_medic_assigned)
                    medicAction.setImageResource(R.drawable.ic_add_circle)
                    medicAction.setOnClickListener { presenter.getMedicList(true) } }

                sharedMedicAction.setOnClickListener { presenter.getMedicList(false) }
                rvSharedMedic.layoutManager = LinearLayoutManager(context)
                rvSharedMedic.setHasFixedSize(true)
                adapter = SharedMedicAdapter(it.patient?.readOnlyMedics!!, this)
                rvSharedMedic.adapter = adapter
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

        if (resultCode == RESULT_OK && requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM) {
            val returnUri = data!!.data
            Glide.with(this).asBitmap().load(returnUri).circleCrop()
                .addListener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        return true
                    }

                    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        resource?.let {
                            val base64 = Utils.convertImage(it)
                            presenter.updateUserThumbnail(base64)
                            val user = MyUser.get(context!!)
                            user?.thumbnail = base64
                            MyUser.set(context!!, user)
                            civAvatar.setImageBitmap(it)
                        }
                        return true
                    }
                })
                .into(civAvatar)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(grantResults.contains(PackageManager.PERMISSION_GRANTED) && requestCode == REQUEST_GALLERY_PERMISSION) {
            this.selectImageFromGallery()
        }
    }

    override fun onMedicSelected(sharedMedic: SharedMedic) {
        presenter.updateCurrentMedic(sharedMedic)
    }

    override fun onSharedMedicSelected(sharedMedic: SharedMedic) {
        presenter.updateSharedMedic(sharedMedic.id, true)
    }

    override fun onMedicListResponse(medics: List<User>, isForMainMedic: Boolean) {
        fragmentManager?.let { SearchMedicFragment.newInstance(medics, this, isForMainMedic).show(it, "dialog") }
    }

    override fun onMedicUpdated(user: User) {
        MyUser.set(context!!, user)
        initUI()
    }

    override fun onSharedMedicRemove(medic: SharedMedic) {
        presenter.updateSharedMedic(medic.id, false)
        adapter.removeSharedMedic(medic)
    }

    override fun onSharedMedicUpdated(sharedMedic: SharedMedic) {
        adapter.addSharedMedic(sharedMedic)
    }

    override fun onPhotoEdited() {

    }
}