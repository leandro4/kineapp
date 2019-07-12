package com.gon.kineapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.fragment_patient_list.*
import android.view.*
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.model.Patient
import com.gon.kineapp.mvp.presenters.PatientListPresenter
import com.gon.kineapp.mvp.views.PatientListView
import com.gon.kineapp.ui.activities.PatientDetailActivity
import com.gon.kineapp.ui.adapters.PatientAdapter
import com.gon.kineapp.utils.Constants

class PatientListFragment : BaseMvpFragment(), PatientListView, PatientAdapter.PatientListener {

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
        fabAddPatient.setOnClickListener {
            Toast.makeText(context, "agregar paciente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initList(patients: MutableList<Patient>) {
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
                Toast.makeText(context, "crear video", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.profile -> {
                Toast.makeText(context, "time line", Toast.LENGTH_SHORT).show()
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

    override fun onErrorCode(message: String) {

    }

    override fun onPatientsReceived(patients: MutableList<Patient>) {
        initList(patients)
    }

    override fun onPatientSelected(patient: Patient) {
        val intent = Intent(activity, PatientDetailActivity::class.java)
        intent.putExtra(Constants.PATIENT_EXTRA, patient)
        activity?.startActivity(intent)
    }
}