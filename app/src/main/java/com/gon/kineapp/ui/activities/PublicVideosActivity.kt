package com.gon.kineapp.ui.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.gon.kineapp.R
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import com.gon.kineapp.ui.fragments.PublicVideosListFragment
import com.gon.kineapp.utils.Constants

class PublicVideosActivity : BaseActivity() {

    private val fragment = PublicVideosListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarTitle(getString(R.string.public_videos_title))
    }

    override fun getFragment(): BaseMvpFragment {
        return fragment
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragment.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.PERMISSIONS_REQUEST_CODE && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            fragment.onPermissionsGranted()
        }
    }
}
