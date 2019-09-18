package com.gon.kineapp.ui.adapters

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.gon.kineapp.R
import com.gon.kineapp.model.Photo
import com.gon.kineapp.model.PhotoTag
import com.gon.kineapp.utils.Utils
import kotlinx.android.synthetic.main.adapter_photo.view.*

class PhotoAdapter(private val photos: MutableList<Photo>, private val callback: PhotoListener): RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    interface PhotoListener {
        fun onPhotoSelected(photo: Photo)
        fun onRemovePhoto(id: String)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PhotoViewHolder {
        return PhotoViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.adapter_photo, p0, false))
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, pos: Int) {
        holder.bind(photos[pos], callback)
    }

    fun addPhoto(photo: Photo) {
        photos.add(photo)
        notifyItemInserted(photos.size - 1)
    }

    fun removePhoto(id: String) {
        val index = photos.indexOfFirst { it.id == id }
        photos.removeAt(index)
        notifyItemRemoved(index)
    }

    class PhotoViewHolder(private var viewItem: View): RecyclerView.ViewHolder(viewItem) {

        fun bind(photo: Photo, callback: PhotoListener) {
            Glide.with(viewItem).load(Utils.convertImage(photo.thumbnail!!)).into(viewItem.ivPhoto)
            viewItem.containerPhoto.setOnClickListener { callback.onPhotoSelected(photo) }
            viewItem.tvTag.visibility = if (photo.tag == PhotoTag.O.name) View.GONE else View.VISIBLE
            viewItem.tvTag.text = photo.tag
            viewItem.ivRemove.setOnClickListener { callback.onRemovePhoto(photo.id) }
        }
    }
}