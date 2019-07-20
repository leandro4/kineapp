package com.gon.kineapp.model

import android.os.Parcelable
import com.gon.kineapp.ui.adapters.PhotoAdapter.Companion.TYPE_PHOTO
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
        var thumbUrl: String,
        var imgUrl: String,
        var tag: String,
        val type: Int = TYPE_PHOTO): Parcelable