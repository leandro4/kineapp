package com.gon.kineapp.ui.adapters

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.gon.kineapp.R
import com.gon.kineapp.model.Video
import kotlinx.android.synthetic.main.adapter_public_video.view.*

class PublicVideoAdapter(private val videos: MutableList<Video>, private val callback: VideoListener): androidx.recyclerview.widget.RecyclerView.Adapter<PublicVideoAdapter.VideoViewHolder>() {

    var removableVideos = true

    interface VideoListener {
        fun onVideoSelected(video: Video)
        fun onRemoveVideoSelected(id: String)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VideoViewHolder {
        return VideoViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.adapter_public_video, p0, false))
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, pos: Int) {
        holder.bind(videos[pos], callback)
        if (!removableVideos) {
            holder.setNotRemovableVideo()
        }
    }

    fun addVideo(video: Video) {
        videos.add(0, video)
        notifyItemInserted(0)
    }

    fun removeVideo(id: String) {
        val index = videos.indexOfFirst { it.id == id }
        videos.removeAt(index)
        notifyItemRemoved(index)
    }

    class VideoViewHolder(private var viewItem: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(viewItem) {

        fun bind(video: Video, callback: VideoListener) {
            video.thumbUrl?.let { Glide.with(viewItem).load(Uri.parse(it)).into(viewItem.ivVideo) }
            viewItem.ivVideo.setOnClickListener { callback.onVideoSelected(video) }
            viewItem.ivRemove.setOnClickListener { callback.onRemoveVideoSelected(video.id) }
            viewItem.tvTitle.text = video.name
        }

        fun setNotRemovableVideo() {
            viewItem.ivRemove.visibility = View.GONE
        }
    }
}