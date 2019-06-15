package com.gon.kineapp.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gon.kineapp.R

class PatientListFragment : BaseMvpFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_patient_list, container, false)
    }

    override fun startPresenter() {

    }

    override fun onErrorCode(message: String) {

    }
}
