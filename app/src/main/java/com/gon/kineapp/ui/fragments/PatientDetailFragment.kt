package com.gon.kineapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.model.Patient
import com.gon.kineapp.model.Session
import com.gon.kineapp.model.Video
import com.gon.kineapp.mvp.presenters.SessionListPresenter
import com.gon.kineapp.mvp.views.SessionListView
import com.gon.kineapp.ui.activities.BaseActivity
import com.gon.kineapp.ui.activities.PrivateVideosActivity
import com.gon.kineapp.ui.activities.SessionDetailActivity
import com.gon.kineapp.ui.adapters.SessionAdapter
import com.gon.kineapp.utils.Constants
import kotlinx.android.synthetic.main.fragment_patient_detail.*

class PatientDetailFragment : BaseMvpFragment(), SessionListView, SessionAdapter.SessionListener {

    private lateinit var patient: Patient
    private lateinit var adapter: SessionAdapter
    private val presenter = SessionListPresenter()

    companion object {
        fun newInstance(patient: Patient): PatientDetailFragment {
            val frag = PatientDetailFragment()
            frag.patient = patient
            return frag
        }

        const val VIEW_SESSION = 2001
        const val VIEW_VIDEOS = 2002
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
            createNewSession()
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
            R.id.videos -> {
                val intent = Intent(context, PrivateVideosActivity::class.java)
                intent.putExtra(Constants.PATIENT_EXTRA, patient)
                activity?.startActivityForResult(intent, VIEW_VIDEOS)
                return true
            }
            R.id.motion_video -> {
                Toast.makeText(context, "timeLine", Toast.LENGTH_SHORT).show()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initList(sessions: MutableList<Session>) {
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

    override fun startPresenter() {
        presenter.attachMvpView(this)
        presenter.getSessions()
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
                patient.videos = videos
            }
        }
    }
}
