package com.gon.kineapp.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.*
import com.gon.kineapp.R
import com.gon.kineapp.model.Photo
import com.gon.kineapp.model.Session
import com.gon.kineapp.mvp.presenters.SessionDetailPresenter
import com.gon.kineapp.mvp.views.SessionDetailView
import com.gon.kineapp.ui.activities.PictureActivity
import com.gon.kineapp.ui.activities.ViewPhotoActivity
import com.gon.kineapp.ui.adapters.PhotoAdapter
import com.gon.kineapp.utils.Constants
import com.gon.kineapp.utils.DialogUtil
import com.gon.kineapp.utils.Utils
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
        tvDate.text = session.date
        etDescription.setText(session.description)
        rvImages.layoutManager = GridLayoutManager(context, COLUMNS, GridLayoutManager.VERTICAL, false)
        rvImages.setHasFixedSize(true)
        adapter = PhotoAdapter(session.photos, this)
        rvImages.adapter = adapter

        if (session.photos.isEmpty()) {
            emptyList.visibility = View.VISIBLE
        }

        fabAddPhoto.setOnClickListener {
            activity?.startActivityForResult(Intent(context, PictureActivity::class.java), TAKE_PICTURE_CODE)
        }

        tvObs.setOnClickListener {
            etDescription.isEnabled = true
            etDescription.requestFocus()
            etDescription.setSelection(etDescription.text.length)
            Utils.showKeyboard(etDescription)
            edited = true
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
        inflater.inflate(R.menu.menu_session, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.save -> {
                if (edited) {
                    Utils.hideKeyboardFrom(etDescription)
                    presenter.saveSession(session.apply { description = etDescription.text.toString() })
                } else {
                    activity?.onBackPressed()
                }
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun startPresenter() {
        presenter.attachMvpView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachMvpView()
    }

    override fun onPhotoSelected(photo: Photo) {
        val intent = Intent(context, ViewPhotoActivity::class.java)
        intent.putExtra(Constants.PHOTO_EXTRA, photo)
        activity?.startActivityForResult(intent, VIEW_PICTURE_CODE)
    }

    override fun onRemovePhoto(id: String) {
        DialogUtil.showOptionsAlertDialog(context!!, getString(R.string.remove_warning_title), getString(R.string.remove_pic_warning_subtitle)) {
            presenter.deletePhoto(id)
        }
    }

    override fun onPhotoDeleted(id: String) {
        setSessionResultIntent()
        adapter.removePhoto(id)
        checkEmptyList()
    }

    override fun onSessionSaved() {
        setSessionResultIntent()
        edited = false
        activity?.onBackPressed()
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
                val photo = it.getParcelableExtra(Constants.PHOTO_EXTRA) as Photo
                adapter.addPhoto(photo)
                setSessionResultIntent()
            }
        }
        else if (requestCode == VIEW_PICTURE_CODE && resultCode == Constants.REMOVED_PHOTO_CODE) {
            data?.let {
                presenter.deletePhoto(it.getParcelableExtra<Photo>(Constants.PHOTO_EXTRA).id)
            }
        }
    }
}