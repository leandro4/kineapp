package com.gon.kineapp.ui.fragments.dialogs

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gon.kineapp.R
import com.gon.kineapp.model.SharedMedic
import com.gon.kineapp.model.User
import com.gon.kineapp.ui.adapters.MedicSelectorAdapter
import kotlinx.android.synthetic.main.fragment_medic_selector.*

class SearchMedicFragment: BaseDialogFragment(), MedicSelectorAdapter.MedicListener {

    private lateinit var medics: List<User>
    private lateinit var callback: MedicListener

    private lateinit var adatper: MedicSelectorAdapter

    interface MedicListener {
        fun onMedicSelected(sharedMedic: SharedMedic)
    }

    companion object {
        fun newInstance(medics: List<User>, callback: MedicListener): SearchMedicFragment {
            val frag = SearchMedicFragment()
            frag.medics = medics
            frag.callback = callback
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_medic_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adatper = MedicSelectorAdapter(medics, this)
        rvMedicsResult.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        rvMedicsResult.setHasFixedSize(true)
        rvMedicsResult.adapter = adatper

        flContainer.setOnClickListener { dismiss() }

        tvResultCount.text = String.format(getString(R.string.medic_selector_count), "")

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let { adatper.filter.filter(s) }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onMedicSelected(sharedMedic: SharedMedic) {
        callback.onMedicSelected(sharedMedic)
        dismiss()
    }
}