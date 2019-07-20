package com.gon.kineapp.ui.adapters

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.gon.kineapp.R
import com.gon.kineapp.model.Photo
import kotlinx.android.synthetic.main.adapter_add_photo.view.*
import kotlinx.android.synthetic.main.adapter_photo.view.*

class PhotoAdapter(private val photos: MutableList<Photo>, private val callback: PhotoListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_ADD = 0
        const val TYPE_PHOTO = 1
    }

    interface PhotoListener {
        fun onPhotoSelected(photo: Photo)
        fun onAddPhotoSelected()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return when (photos[p1].type) {
            TYPE_PHOTO -> PhotoViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.adapter_photo, p0, false))
            else -> AddPhotoViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.adapter_add_photo, p0, false))
        }
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun getItemViewType(position: Int): Int {
        return photos[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        when (photos[pos].type) {
            TYPE_PHOTO -> (holder as PhotoViewHolder).bind(photos[pos], callback)
            else -> (holder as AddPhotoViewHolder).bind(callback)
        }
    }

    class PhotoViewHolder(private var viewItem: View): RecyclerView.ViewHolder(viewItem) {

        fun bind(photo: Photo, callback: PhotoListener) {
            Glide.with(viewItem).load(Uri.parse(photo.thumbUrl)).into(viewItem.ivPhoto)
            viewItem.containerPhoto.setOnClickListener { callback.onPhotoSelected(photo) }
        }
    }

    class AddPhotoViewHolder(private val viewItem: View): RecyclerView.ViewHolder(viewItem) {

        fun bind(callback: PhotoListener) {
            viewItem.containerAdd.setOnClickListener { callback.onAddPhotoSelected() }
        }
    }
}