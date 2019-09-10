package com.gon.kineapp.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gon.kineapp.R
import com.gon.kineapp.model.Patient
import com.gon.kineapp.model.User
import kotlinx.android.synthetic.main.adapter_patient.view.*

class PatientAdapter(private val patients: MutableList<User>, private val callback: PatientListener): RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    interface PatientListener {
        fun onPatientSelected(patient: User)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PatientViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.adapter_patient, p0, false)
        return PatientViewHolder(v)
    }

    override fun getItemCount(): Int {
        return patients.size
    }

    override fun onBindViewHolder(holder: PatientViewHolder, pos: Int) {
        holder.bind(patients[pos], callback)
    }

    class PatientViewHolder(private var viewItem: View): RecyclerView.ViewHolder(viewItem) {

        fun bind(patient: User, callback: PatientListener) {
            viewItem.tvName.text = String.format("%s %s", patient.name, patient.surname)
            viewItem.tvNumber.text = patient.dni
            viewItem.flContent.setOnClickListener {
                callback.onPatientSelected(patient)
            }
        }
    }
}