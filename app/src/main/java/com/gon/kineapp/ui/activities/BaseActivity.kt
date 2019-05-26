package com.gon.kineapp.ui.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.gon.kineapp.R
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import kotlinx.android.synthetic.main.activity_base_content.*

abstract class BaseActivity: AppCompatActivity(), BaseMvpFragment.ActivityProgress {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_content)
        setSupportActionBar(toolbar)
        loadFragment()
    }

    private fun loadFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContent, getFragment())
        transaction.commit()
    }

    protected abstract fun getFragment(): BaseMvpFragment

    protected fun setToolbarTitle(resTitleId: Int) {
        supportActionBar?.title = getString(resTitleId)
    }

    protected fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun showProgress() {
        progressView.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressView.visibility = View.GONE
    }
}