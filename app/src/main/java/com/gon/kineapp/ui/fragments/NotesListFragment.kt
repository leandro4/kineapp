package com.gon.kineapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gon.kineapp.R

class NotesListFragment: BaseMvpFragment() {

    //var presenter: Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startPresenter()
    }

    override fun startPresenter() {
        // inicializar presenter y attachear:
        //presenter?.attachMvpView(this)
    }

    override fun onErrorCode(message: String) {
        showErrorMessage(getString(R.string.generic_error_message))
    }

    override fun onDestroy() {
        // presenter?.dettachMvpView()
        super.onDestroy()
    }

}