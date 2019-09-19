package com.gon.kineapp.ui.activities

import android.content.Intent
import android.os.Bundle
import com.gon.kineapp.R
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import com.gon.kineapp.ui.fragments.PublicVideosListFragment

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
}
