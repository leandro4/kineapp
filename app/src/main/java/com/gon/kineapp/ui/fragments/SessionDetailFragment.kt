package com.gon.kineapp.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.gon.kineapp.R
import com.gon.kineapp.model.Photo
import com.gon.kineapp.model.PhotoTag
import com.gon.kineapp.model.Session
import com.gon.kineapp.mvp.presenters.SessionDetailPresenter
import com.gon.kineapp.mvp.views.SessionDetailView
import com.gon.kineapp.ui.activities.PictureActivity
import com.gon.kineapp.ui.activities.ViewPhotoActivity
import com.gon.kineapp.ui.adapters.PhotoAdapter
import com.gon.kineapp.utils.*
import kotlinx.android.synthetic.main.fragment_session_detail.*

class SessionDetailFragment : BaseMvpFragment(), PhotoAdapter.PhotoListener, SessionDetailView {

    private lateinit var session: Session
    private var edited = false
    private lateinit var adapter: PhotoAdapter
    private var presenter = SessionDetailPresenter()

    companion object {
        fun newInstance(session: Session): SessionDetailFragment {
            val frag = SessionDetailFragment()
            frag.session = session
            return frag
        }

        private const val COLUMNS = 4

        const val TAKE_PICTURE_CODE = 1000
        const val VIEW_PICTURE_CODE = 1001
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.gon.kineapp.R.layout.fragment_session_detail, container, false)
        setHasOptionsMenu(true)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        tvDate.text = Utils.formatDate(session.date)
        etDescription.setText(session.description)
        rvImages.layoutManager = androidx.recyclerview.widget.GridLayoutManager(
            context,
            COLUMNS,
            RecyclerView.VERTICAL,
            false
        )
        rvImages.setHasFixedSize(true)
        adapter = PhotoAdapter(session.photos, this)
        rvImages.adapter = adapter

        checkEmptyList()

        fabAddPhoto.setOnClickListener {
            activity?.startActivityForResult(Intent(context, PictureActivity::class.java), TAKE_PICTURE_CODE)
        }

        if (session.readOnly) {
            adapter.photosRemovables = false
        }
        if (session.readOnly || MyUser.get(context!!)?.isPatient()!!) {
            emptyList.text = getString(R.string.empty_photos_no_medic)
            fabAddPhoto.hide()
            tvObs.setCompoundDrawables(null, null, null, null)
        } else {
            tvObs.setOnClickListener {
                etDescription.isEnabled = true
                etDescription.requestFocus()
                etDescription.setSelection(etDescription.text.length)
                Utils.showKeyboard(etDescription)
                edited = true
            }
        }
    }

    override fun handleBackPressed(): Boolean {
        if (edited) {
            DialogUtil.showOptionsAlertDialog(context!!,
                getString(R.string.caution_title),
                getString(R.string.exit_without_saving_msg)) {
                    Utils.hideKeyboardFrom(etDescription)
                    activity?.finish() }
            return true
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (MyUser.get(context!!)?.isPatient()!!) {
            inflater.inflate(R.menu.menu_session_delete, menu)
        } else if (!session.readOnly) {
            inflater.inflate(R.menu.menu_session, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                if (edited) {
                    Utils.hideKeyboardFrom(etDescription)
                    presenter.saveSession(session.apply { description = etDescription.text.toString() })
                } else {
                    activity?.onBackPressed()
                }
                return true
            }
            R.id.delete -> {
                DialogUtil.showGenericAlertDialogConfirm(context!!, getString(R.string.remove_warning_title), getString(R.string.delete_session_message)) {
                    presenter.deleteSession(session.id)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun startPresenter() {
        presenter.attachMvpView(this)
    }

    override fun onDestroy() {
        presenter.detachMvpView()
        super.onDestroy()
    }

    override fun onPhotoSelected(photo: Photo) {
        //presenter.getPhoto(photo.id)
        presenter.getPhotos(session.id, photo.id)
    }

    override fun onPhotoLoaded(photo: Photo) {
        val intent = Intent(context, ViewPhotoActivity::class.java)
        intent.putExtra(Constants.PHOTO_EXTRA, photo)
        activity?.startActivityForResult(intent, VIEW_PICTURE_CODE)
    }

    override fun onPhotosReceived(photos: ArrayList<Photo>, photoIdSelected: String) {
        PhotosRepository.photos = photos
        val intent = Intent(context, ViewPhotoActivity::class.java)
        intent.putExtra(Constants.PHOTO_EXTRA, photoIdSelected)
        activity?.startActivityForResult(intent, VIEW_PICTURE_CODE)
    }

    override fun onRemovePhoto(id: String) {
        DialogUtil.showOptionsAlertDialog(context!!, getString(R.string.remove_warning_title), getString(R.string.remove_pic_warning_subtitle)) { removePhoto(id) }
    }

    override fun onPhotoDeleted() {
        setSessionResultIntent()
    }

    override fun onSessionSaved() {
        setSessionResultIntent()
        edited = false
        activity?.onBackPressed()
    }

    override fun onSessionDeleted() {
        val intent = Intent()
        intent.putExtra(Constants.SESSION_EXTRA, session)
        activity?.setResult(Constants.DELETED_SESSION_CODE, intent)
        activity?.finish()
    }

    override fun onPhotoUploaded(photo: Photo) {
        adapter.addPhoto(photo)
        setSessionResultIntent()
    }

    private fun removePhoto(id: String) {
        presenter.deletePhoto(id)
        adapter.removePhoto(id)
        checkEmptyList()
    }

    private fun checkEmptyList() {
        emptyList.visibility = if (session.photos.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun setSessionResultIntent() {
        val intent = Intent()
        intent.putExtra(Constants.SESSION_EXTRA, session)
        activity?.setResult(Constants.EDITED_SESSION_CODE, intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TAKE_PICTURE_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                checkEmptyList()
                val photo = it.getStringExtra(Constants.PHOTO_EXTRA)
                val tag = it.getStringExtra(Constants.PHOTO_TAG_EXTRA)
                presenter.uploadPhoto(session.id, photo, tag)
            }
        }
        else if (requestCode == VIEW_PICTURE_CODE && resultCode == Constants.REMOVED_PHOTO_CODE) {
            data?.let {
                removePhoto(it.getStringExtra(Constants.PHOTO_EXTRA))
            }
        }
    }
}