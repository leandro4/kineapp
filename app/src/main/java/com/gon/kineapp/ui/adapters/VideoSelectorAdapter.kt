package com.gon.kineapp.ui.adapters

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.gon.kineapp.R
import com.gon.kineapp.model.Video
import kotlinx.android.synthetic.main.adapter_video_selector.view.*

class VideoSelectorAdapter(private var videos: MutableList<Video>, private val callback: VideoListener): RecyclerView.Adapter<VideoSelectorAdapter.VideoViewHolder>() {

    interface VideoListener {
        fun onVideoSelected(video: Video)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VideoViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.adapter_video_selector, p0, false)
        return VideoViewHolder(v)
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, pos: Int) {
        holder.bind(videos[pos], callback)
    }

    class VideoViewHolder(private var viewItem: View): RecyclerView.ViewHolder(viewItem) {

        fun bind(video: Video, callback: VideoListener) {
            video.thumbUrl?.let { Glide.with(viewItem).load(Uri.parse(it)).into(viewItem.ivThumb) }
            viewItem.tvName.text = video.name
            viewItem.flContent.setOnClickListener {
                callback.onVideoSelected(video)
            }
        }
    }
}