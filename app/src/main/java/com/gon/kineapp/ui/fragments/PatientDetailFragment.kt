package com.gon.kineapp.ui.fragments

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.vincent.videocompressor.VideoCompress
import java.io.File
import android.os.Environment
import android.util.Log

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
        const val VIEW_VIDEOS = 2002
        const val TAKE_VIDEO = 2003
        const val EDIT_ROUTINE = 2004
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.gon.kineapp.R.layout.fragment_patient_detail, container, false)
        setHasOptionsMenu(true)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
    }

    private fun createNewSession() {
        presenter.createSession(patient.id)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_patient_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.exercises -> {
                /*val intent = Intent(context, PrivateVideosActivity::class.java)
                intent.putExtra(Constants.PATIENT_EXTRA, patient)
                activity?.startActivityForResult(intent, VIEW_VIDEOS)*/

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
        val intent = Intent(activity, SessionDetailActivity::class.java)
        intent.putExtra(Constants.SESSION_EXTRA, session)
        intent.putExtra(Constants.NAME_EXTRA, patient.name)
        activity?.startActivityForResult(intent, VIEW_SESSION)
    }

    override fun onPhotosByTagReceived(photos: ArrayList<Photo>) {
        val intent = Intent(context, TimeLineActivity::class.java)
        PhotosRepository.photos = photos
        activity?.startActivityForResult(intent, EDIT_ROUTINE)
    }

    override fun onVideoUploaded(video: Video) {
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
        else if (requestCode == VIEW_VIDEOS && resultCode == Constants.EDITED_VIDEOS_CODE) {
            data?.let {
                val videos = it.getParcelableArrayListExtra<Video>(Constants.VIDEO_EXTRA)
                patient.patient?.videos = videos
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
                RESULT_CANCELED -> Toast.makeText(context, "Video recording cancelled.", Toast.LENGTH_LONG).show()
                else -> Toast.makeText(context, "Failed to record video", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun compressVideo(uri: Uri) {
        val cursor = activity!!.contentResolver.query(uri,null,null,null,null)
        if (cursor == null) {
            onErrorCode(getString(R.string.error_take_video))
            return
        }

        cursor.moveToFirst()
        val videoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
        cursor.close()

        val folderFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        val outFile = folderFile + File.separator + getString(R.string.app_name) + System.currentTimeMillis() + getString(R.string.video_extension)

        VideoCompress.compressVideoLow(videoPath, outFile, object : VideoCompress.CompressListener {
                override fun onStart() {
                    showProgressView()
                }

                override fun onSuccess() {
                    presenter.uploadVideo(outFile, "Flexiones")
                }

                override fun onFail() {
                    hideProgressView()
                    onErrorCode(getString(R.string.error_comprees_video))
                }

                override fun onProgress(percent: Float) {}
            })
    }
}