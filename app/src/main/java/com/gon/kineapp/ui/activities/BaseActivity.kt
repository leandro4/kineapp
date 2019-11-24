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
import android.view.WindowManager
import com.gonanimationlib.animations.Animate

abstract class BaseActivity: LockableActivity(), BaseMvpFragment.ActivityProgress {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
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

    override fun onBackPressed() {
        if (supportFragmentManager.fragments[0] != null && !(supportFragmentManager.fragments[0] as BaseMvpFragment).handleBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun showProgress() {
        progressView.visibility = View.VISIBLE
        Animate.ALPHA(1f).duration(Animate.DURATION_MEDIUM).startAnimation(progressView)
    }

    override fun hideProgress() {
        Animate.ALPHA(0f).duration(Animate.DURATION_MEDIUM).onEnd { progressView.visibility = View.GONE }.startAnimation(progressView)
    }

    override fun startActivity(intent: Intent?) {
        val options = ActivityOptions.makeSceneTransitionAnimation(this)
        startActivity(intent, options.toBundle())
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        val options = ActivityOptions.makeSceneTransitionAnimation(this)
        startActivityForResult(intent, requestCode, options.toBundle())
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