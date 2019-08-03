package com.gon.kineapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.fragment_patient_list.*
import android.view.*
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.model.Patient
import com.gon.kineapp.model.User
import com.gon.kineapp.mvp.presenters.PatientListPresenter
import com.gon.kineapp.mvp.views.PatientListView
import com.gon.kineapp.ui.activities.PatientDetailActivity
import com.gon.kineapp.ui.activities.ProfileActivity
import com.gon.kineapp.ui.activities.PublicVideosActivity
import com.gon.kineapp.ui.adapters.PatientAdapter
import com.gon.kineapp.utils.Constants

class PatientsListFragment : BaseMvpFragment(), PatientListView, PatientAdapter.PatientListener {

    private val presenter = PatientListPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.gon.kineapp.R.layout.fragment_patient_list, container, false)
        setHasOptionsMenu(true)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
    }

    private fun initList(patients: MutableList<User>) {
        rvPatients.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvPatients.setHasFixedSize(true)
        rvPatients.adapter = PatientAdapter(patients, this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.videos -> {
                activity?.startActivity(Intent(context, PublicVideosActivity::class.java))
                return true
            }
            R.id.profile -> {
                activity?.startActivity(Intent(context, ProfileActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun startPresenter() {
        presenter.attachMvpView(this)
        presenter.getPatientList()
    }

    override fun onDestroy() {
        presenter.detachMvpView()
        super.onDestroy()
    }

    override fun onPatientsReceived(patients: MutableList<User>) {
        initList(patients)
    }

    override fun onPatientSelected(patient: User) {
        val intent = Intent(activity, PatientDetailActivity::class.java)
        intent.putExtra(Constants.PATIENT_EXTRA, patient)
        activity?.startActivity(intent)
    }
}