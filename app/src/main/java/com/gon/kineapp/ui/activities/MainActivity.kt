package com.gon.kineapp.ui.activities

import android.os.Bundle
import com.gon.kineapp.R
import com.gon.kineapp.ui.fragments.BaseMvpFragment
import com.gon.kineapp.ui.fragments.NotesListFragment
import kotlinx.android.synthetic.main.activity_base_content.*

class MainActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbarTitle("Bienvenido!")
    }

    override fun getFragment(): BaseMvpFragment {
        return NotesListFragment().apply { activityProgress = this@MainActivity }
    }
}