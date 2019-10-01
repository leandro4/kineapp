package com.gon.kineapp.ui.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gon.kineapp.R
import kotlinx.android.synthetic.main.fragment_input.*

class InputDialogFragment: BaseDialogFragment() {

    private var callback: InputListener? = null
    private var title = ""

    interface InputListener {
        fun onInputDone(input: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTitle.text = title

        fabInputDone.setOnClickListener {
            callback?.onInputDone(etInput.text.toString())
            dismiss()
        }
    }

    fun setTitle(title: String): InputDialogFragment {
        this.title = title
        return this
    }

    fun setCallback(listener: InputListener): InputDialogFragment {
        this.callback = listener
        return this
    }

    fun setCancellable(cancellable: Boolean): InputDialogFragment {
        isCancelable = cancellable
        return this
    }
}