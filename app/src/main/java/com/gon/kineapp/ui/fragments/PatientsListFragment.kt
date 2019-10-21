package com.gon.kineapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.fragment_patient_list.*
import android.view.*
import com.gon.kineapp.R
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
    private val adapterMyPatients = PatientAdapter(ArrayList(), this)
    private val adapterOtherPatients = PatientAdapter(ArrayList(), this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_patient_list, container, false)
        setHasOptionsMenu(true)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        swipeRefresh.setOnRefreshListener {
            presenter.getPatientList()
        }
        presenter.getPatientList()
        swipeRefresh.isRefreshing = true

        rvPatients.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvPatients.setHasFixedSize(true)
        rvPatients.adapter = adapterMyPatients

        rvReadOnlyPatients.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvReadOnlyPatients.setHasFixedSize(true)
        rvReadOnlyPatients.adapter = adapterOtherPatients
    }

    private fun updateList(patients: MutableList<User>, readOnlyPatients: MutableList<User>) {
        emptyListMyPatients.visibility = if (patients.isEmpty()) View.VISIBLE else View.GONE
        emptyListOtherPatients.visibility = if (readOnlyPatients.isEmpty()) View.VISIBLE else View.GONE
        adapterMyPatients.setPatients(patients)
        adapterOtherPatients.setPatients(readOnlyPatients)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_medics_home, menu)
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
        context?.let { presenter.syncCurrentUser(it) }
    }

    override fun onDestroy() {
        presenter.detachMvpView()
        super.onDestroy()
    }

    override fun onPatientsReceived(patients: MutableList<User>, readOnlyPatients: MutableList<User>) {
        updateList(patients, readOnlyPatients)
        swipeRefresh.isRefreshing = false
    }

    override fun onPatientSelected(patient: User) {
        val intent = Intent(activity, PatientDetailActivity::class.java)
        intent.putExtra(Constants.PATIENT_EXTRA, patient)
        activity?.startActivity(intent)
    }
}