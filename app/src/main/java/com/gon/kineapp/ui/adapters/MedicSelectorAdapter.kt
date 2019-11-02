package com.gon.kineapp.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.gon.kineapp.R
import com.gon.kineapp.model.SharedMedic
import com.gon.kineapp.model.User
import kotlinx.android.synthetic.main.adapter_medic_selector.view.*

class MedicSelectorAdapter(private val medics: List<User>, private val callback: MedicListener)
    : androidx.recyclerview.widget.RecyclerView.Adapter<MedicSelectorAdapter.MedicViewHolder>(), Filterable {

    private var filteredMedics: List<User>

    interface MedicListener {
        fun onMedicSelected(sharedMedic: SharedMedic)
    }

    init {
        filteredMedics = ArrayList()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MedicViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.adapter_medic_selector, p0, false)
        return MedicViewHolder(v)
    }

    override fun getItemCount(): Int {
        return filteredMedics.size
    }

    override fun onBindViewHolder(holder: MedicViewHolder, pos: Int) {
        holder.bind(filteredMedics[pos], callback)
    }

    override fun getFilter(): Filter {

        return object : Filter() {

            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                filteredMedics = filterResults.values as ArrayList<User>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty())
                    medics
                else
                    medics.filter {
                        it.name.toLowerCase().contains(queryString) ||
                                it.surname.toLowerCase().contains(queryString) ||
                                it.medic?.license?.toLowerCase()?.contains(queryString) ?: false
                    }
                return filterResults
            }
        }
    }

    class MedicViewHolder(private var viewItem: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(viewItem) {

        fun bind(medic: User, callback: MedicListener) {
            viewItem.tvFirstName.text = String.format("%s %s", medic.name, medic.surname)
            viewItem.tvLicense.text = medic.medic?.license
            viewItem.container.setOnClickListener {
                callback.onMedicSelected(SharedMedic(medic.id, medic.name, medic.surname))
            }
        }
    }
}