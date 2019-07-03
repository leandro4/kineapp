package com.gon.kineapp.ui.fragments.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import com.gon.kineapp.R
import com.gonanimationlib.animations.Animate
import com.gonanimationlib.animations.CompZoom

open class BaseDialogFragment: DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Animate.ZOOM(CompZoom.KIND.IN).duration(Animate.DURATION_LARGE).startAnimation(view)
    }
}