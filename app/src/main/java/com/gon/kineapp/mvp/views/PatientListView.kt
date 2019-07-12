package com.gon.kineapp.mvp.views

import com.gon.kineapp.model.Patient

interface PatientListView: BaseView {

    fun onPatientsReceived(patients: MutableList<Patient>)

}