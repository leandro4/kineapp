package com.gon.kineapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.model.Patient
import com.gon.kineapp.model.Session
import com.gon.kineapp.ui.activities.BaseActivity
import com.gon.kineapp.ui.activities.SessionDetailActivity
import com.gon.kineapp.utils.Constants
import kotlinx.android.synthetic.main.fragment_patient_detail.*

class PatientDetailFragment : BaseMvpFragment() {

    private lateinit var patient: Patient

    companion object {
        fun newInstance(patient: Patient): PatientDetailFragment {
            val frag = PatientDetailFragment()
            frag.patient = patient
            return frag
        }
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
            Toast.makeText(context, "agregrar sesión", Toast.LENGTH_SHORT).show()
        }

        sessionSimulator.setOnClickListener {
            goToSessionDetail(Session("Juanitou"))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_patient_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.videos -> {
                Toast.makeText(context, "videos", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.motion_video -> {
                Toast.makeText(context, "perfil", Toast.LENGTH_SHORT).show()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun goToSessionDetail(session: Session) {
        val intent = Intent(activity, SessionDetailActivity::class.java)
        intent.putExtra(Constants.SESSION_EXTRA, session)
        activity?.startActivity(intent)
    }

    override fun startPresenter() {

    }

    override fun onErrorCode(message: String) {

    }
}
