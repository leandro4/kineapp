package com.gon.kineapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.gon.kineapp.R
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import kotlinx.android.synthetic.main.activity_base_content.*
import android.app.ActivityOptions

abstract class BaseActivity: LockableActivity(), BaseMvpFragment.ActivityProgress {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAnimation()
        setContentView(R.layout.activity_base_content)
        setSupportActionBar(toolbar)
        loadFragment()

        if (enabledBackButton()) {
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun loadFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContent, getFragment())
        transaction.commit()
    }

    protected abstract fun getFragment(): BaseMvpFragment

    protected open fun enabledBackButton(): Boolean {
        return true
    }

    fun setToolbarTitle(resTitleId: Int) {
        supportActionBar?.title = getString(resTitleId)
    }

    fun setToolbarTitle(title: String) {
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

    override fun startActivity(intent: Intent?) {
        val options = ActivityOptions.makeSceneTransitionAnimation(this)
        startActivity(intent, options.toBundle())
    }

    private fun setAnimation() {
        val slide = Slide()
        slide.slideEdge = Gravity.START
        slide.duration = 400
        slide.interpolator = DecelerateInterpolator()
        window.exitTransition = slide
        window.enterTransition = slide
    }
}