package com.gon.kineapp.mvp.views

import com.gon.kineapp.model.User

interface PatientListView: BaseView {

    fun onPatientsReceived(patients: MutableList<User>, readOnlyPatients: MutableList<User>)

}