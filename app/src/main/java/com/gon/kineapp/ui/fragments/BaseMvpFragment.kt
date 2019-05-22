package com.gon.kineapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.View
import com.gon.kineapp.R
import com.gon.kineapp.mvp.views.BaseView

abstract class BaseMvpFragment: Fragment(), BaseView {

    var progressBar: View? = null
    var activityProgress: ActivityProgress? = null

    interface ActivityProgress {
        fun showProgress()
        fun hideProgress()
    }

    protected fun showErrorMessage(message: String) {
        context?.let {
            AlertDialog.Builder(it)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                    .show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startPresenter()
    }

    abstract fun startPresenter()

    override fun getMvpContext(): Context? {
        return context
    }

    override fun onError(var1: Throwable) {
        showErrorMessage(var1.message?: run { getString(R.string.generic_error_message) })
    }

    override fun onNoInternetConnection() {
        showErrorMessage(getString(R.string.warning_no_conection))
    }

    override fun showProgressView() {
        progressBar?.visibility = View.VISIBLE
        activityProgress?.showProgress()
    }

    override fun hideProgressView() {
        progressBar?.visibility = View.GONE
        activityProgress?.hideProgress()
    }
}