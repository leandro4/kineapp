package com.gon.kineapp.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.model.*
import com.gon.kineapp.mvp.presenters.SessionListPresenter
import com.gon.kineapp.mvp.views.SessionListView
import com.gon.kineapp.ui.activities.*
import com.gon.kineapp.ui.adapters.SessionAdapter
import com.gon.kineapp.utils.*
import kotlinx.android.synthetic.main.fragment_patient_detail.*
import com.gon.kineapp.ui.fragments.dialogs.InputDialogFragment

class PatientDetailFragment : BaseMvpFragment(), SessionListView, SessionAdapter.SessionListener {

    private lateinit var patient: User
    private lateinit var adapter: SessionAdapter
    private var sessions: MutableList<Session>? = null
    private val presenter = SessionListPresenter()

    companion object {
        fun newInstance(patient: User): PatientDetailFragment {
            val frag = PatientDetailFragment()
            frag.patient = patient
            return frag
        }

        const val VIEW_SESSION = 2001
        const val TAKE_VIDEO = 2002
        const val EDIT_ROUTINE = 2003
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.gon.kineapp.R.layout.fragment_patient_detail, container, false)
        setHasOptionsMenu(true)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (patient == null) {
            activity?.finish()
            return
        }
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        (activity as BaseActivity).setToolbarTitle(String.format(getString(R.string.patient_sessions_title), patient.name))
        fabAddSession.setOnClickListener {
            DialogUtil.showOptionsAlertDialog(context!!, getString(R.string.create_session_title), getString(R.string.create_session_msg)) {
                createNewSession()
            }
        }
        if (patient.patient?.readOnly!!) {
            fabAddSession.hide()
        }
    }

    private fun createNewSession() {
        presenter.createSession(patient.id)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (patient.patient?.readOnly!!) {
            inflater.inflate(R.menu.menu_read_only_patient_detail, menu)
        } else {
            inflater.inflate(R.menu.menu_patient_detail, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.exercises -> {
                val intent = Intent(context, EditPatientRoutineActivity::class.java)
                intent.putExtra(Constants.USER_EXTRA, patient)
                activity?.startActivityForResult(intent, EDIT_ROUTINE)
                return true
            }
            R.id.motion_video -> goToTimeLine()
            R.id.take_video -> Utils.takeVideo(activity!!, TAKE_VIDEO)
        }
        return super.onOptionsItemSelected(item)
    }

    fun onPermissionsGranted() {
        Utils.takeVideo(activity!!, TAKE_VIDEO)
    }

    private fun goToTimeLine() {
        val options = ArrayList<PhotoTag>().apply { PhotoTag.values().forEach { if (it != PhotoTag.O) add(it) } }
        DialogUtil.showChooserListDialog(context!!, getString(R.string.choose_tag_title), options.map { it.getCompleteName() }) {
            PhotoTag.getTag(PhotoTag.values()[it].getCompleteName())?.name?.let { tag -> presenter.getPhotosByTag(patient.id, tag) }
        }
    }

    private fun initList(sessions: MutableList<Session>) {
        this.sessions = sessions
        rvSessions.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvSessions.setHasFixedSize(true)
        adapter = SessionAdapter(sessions, this)
        rvSessions.adapter = adapter
    }

    override fun onSessionsReceived(sessions: MutableList<Session>) {
        initList(sessions)
    }

    override fun onSessionCreated(session: Session) {
        adapter.addSession(session)
        onSessionSelected(session)
    }

    override fun onSessionSelected(session: Session) {
        session.readOnly = patient.patient?.readOnly!!
        val intent = Intent(activity, SessionDetailActivity::class.java)
        intent.putExtra(Constants.SESSION_EXTRA, session)
        intent.putExtra(Constants.NAME_EXTRA, patient.name)
        activity?.startActivityForResult(intent, VIEW_SESSION)
    }

    override fun onPhotosByTagReceived(photos: ArrayList<Photo>) {
        if (photos.isEmpty()) {
            Toast.makeText(context, getString(R.string.timeline_no_photos), Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(context, TimeLineActivity::class.java)
        PhotosRepository.photos = photos
        activity?.startActivityForResult(intent, EDIT_ROUTINE)
    }

    override fun onVideoUploaded(video: Video) {
        DialogUtil.showGenericAlertDialog(context!!, getString(R.string.success_video_title), getString(R.string.success_video_msg))
        Toast.makeText(context!!, video.id, Toast.LENGTH_SHORT).show()
    }

    override fun startPresenter() {
        presenter.attachMvpView(this)
        presenter.getSessions(patient.id)
    }

    override fun onDestroy() {
        presenter.detachMvpView()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == VIEW_SESSION && resultCode == Constants.EDITED_SESSION_CODE) {
            data?.let {
                val session = it.getParcelableExtra<Session>(Constants.SESSION_EXTRA)
                adapter.updateSession(session)
            }
        }
        else if (requestCode == EDIT_ROUTINE && resultCode == Constants.EDITED_ROUTINE_CODE) {
            data?.let {
                val user = it.getParcelableExtra<User>(Constants.USER_EXTRA)
                patient.patient?.routine = user.patient?.routine
            }
        }
        else if (requestCode == TAKE_VIDEO) {
            when (resultCode) {
                RESULT_OK -> data?.data?.let { compressVideo(it) }
                //RESULT_CANCELED -> Toast.makeText(context, "Video recording cancelled.", Toast.LENGTH_LONG).show()
                else -> onErrorCode(getString(R.string.error_take_video))
            }
        }
    }

    private fun compressVideo(uri: Uri) {
        activity?.contentResolver?.let {
            showProgressView()
            Utils.compressVideo(uri, it, { msg ->
                    hideProgressView()
                    onErrorCode(msg) }, this::uploadVideo)
        }
    }

    private fun uploadVideo(path: String) {
        InputDialogFragment()
            .setTitle(getString(R.string.input_video_title))
            .setCancellable(false)
            .setCallback(object : InputDialogFragment.InputListener {
                override fun onInputDone(input: String) {
                    presenter.uploadVideo(path, input)
                }
            }).show(fragmentManager, "inputDialog")
    }
}