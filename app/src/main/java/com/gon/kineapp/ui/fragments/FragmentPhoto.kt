package com.gon.kineapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gon.kineapp.R
import com.gon.kineapp.utils.ImageLoader
import com.gon.kineapp.utils.Utils
import kotlinx.android.synthetic.main.fragment_photo.*

class FragmentPhoto(val content: String): Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ImageLoader.load(context, Utils.convertImage(content)).into(ivPhoto)
    }
}