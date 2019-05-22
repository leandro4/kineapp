package com.gon.kineapp.ui.activities

import android.os.Bundle
import com.gon.kineapp.R
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import com.gon.kineapp.ui.fragments.NotesListFragment

class MainActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarTitle("Bienvenide!")
    }

    override fun getFragment(): BaseMvpFragment {
        return NotesListFragment().apply { activityProgress = this@MainActivity }
    }
}