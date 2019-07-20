package com.gon.kineapp.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.model.Photo
import com.gon.kineapp.model.Session
import com.gon.kineapp.ui.activities.BaseActivity
import com.gon.kineapp.ui.adapters.PhotoAdapter
import com.gon.kineapp.utils.DialogUtil
import com.gon.kineapp.utils.Utils
import kotlinx.android.synthetic.main.fragment_session_detail.*

class SessionDetailFragment : BaseMvpFragment(), PhotoAdapter.PhotoListener {

    private lateinit var session: Session
    private var edited = false

    companion object {
        fun newInstance(session: Session): SessionDetailFragment {
            val frag = SessionDetailFragment()
            frag.session = session
            return frag
        }

        private const val COLUMNS = 4
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
        (activity as BaseActivity).setToolbarTitle(session.patientName)

        etDescription.setText(session.description)
        rvImages.layoutManager = GridLayoutManager(context, COLUMNS, GridLayoutManager.VERTICAL, false)
        rvImages.setHasFixedSize(true)
        rvImages.adapter = PhotoAdapter(session.photos, this)

        fabAddPhoto.setOnClickListener {
            Toast.makeText(context, "Tomar foto", Toast.LENGTH_SHORT).show()
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
                Utils.hideKeyboardFrom(etDescription)
                showProgressView()
                Handler().postDelayed({
                    edited = false
                    hideProgressView()
                    activity?.onBackPressed()
                }, 1000)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun startPresenter() {

    }

    override fun onErrorCode(message: String) {}

    override fun onPhotoSelected(photo: Photo) {
        Toast.makeText(context, "Ver foto", Toast.LENGTH_SHORT).show()
    }

    override fun onAddPhotoSelected() {
        Toast.makeText(context, "Tomar foto", Toast.LENGTH_SHORT).show()
    }
}
