package com.gon.kineapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gon.kineapp.R
import com.gon.kineapp.model.SharedMedic
import kotlinx.android.synthetic.main.adapter_shared_medic.view.*

class SharedMedicAdapter(private var medics: MutableList<SharedMedic>, private val callback: SharedMedicListener): androidx.recyclerview.widget.RecyclerView.Adapter<SharedMedicAdapter.SharedMedicViewHolder>() {

    interface SharedMedicListener {
        fun onSharedMedicRemove(medic: SharedMedic)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SharedMedicViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.adapter_shared_medic, p0, false)
        return SharedMedicViewHolder(v)
    }

    override fun getItemCount(): Int {
        return medics.size
    }

    override fun onBindViewHolder(holder: SharedMedicViewHolder, pos: Int) {
        holder.bind(medics[pos], callback)
    }

    fun addSharedMedic(sharedMedic: SharedMedic) {
        medics.add(sharedMedic)
        notifyItemInserted(medics.size - 1)
    }

    fun removeSharedMedic(sharedMedic: SharedMedic) {
        val index = medics.indexOfFirst { it.id == sharedMedic.id }
        medics.removeAt(index)
        notifyItemRemoved(index)
    }

    class SharedMedicViewHolder(private var viewItem: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(viewItem) {

        fun bind(medic: SharedMedic, callback: SharedMedicListener) {
            viewItem.tvName.text = String.format("%s %s", medic.name, medic.surname)
            //viewItem.tvLicense.text = medic.
            viewItem.flContent.setOnClickListener {
                callback.onSharedMedicRemove(medic)
            }
        }
    }
}