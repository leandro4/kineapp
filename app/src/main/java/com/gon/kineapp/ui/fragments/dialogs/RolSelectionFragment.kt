package com.gon.kineapp.ui.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gon.kineapp.R
import com.gon.kineapp.utils.LockerAppCallback
import com.gonanimationlib.animations.Animate
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_rol_selection.*
import kotlinx.android.synthetic.main.fragment_secret_question.*
import java.util.concurrent.TimeUnit

class RolSelectionFragment: BaseDialogFragment() {

    private var listener: ResponseListener? = null

    interface ResponseListener {
        fun onProfessionalSelected()
        fun onPatientSelected()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rol_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        initUI()
    }

    private fun initUI() {
        tvProfessional.setOnClickListener {
            listener?.onProfessionalSelected()
            dismiss()
        }
        tvPatient.setOnClickListener {
            listener?.onPatientSelected()
            dismiss()
        }
    }

    fun setListener(listener: ResponseListener): RolSelectionFragment {
        this.listener = listener
        return this
    }
}