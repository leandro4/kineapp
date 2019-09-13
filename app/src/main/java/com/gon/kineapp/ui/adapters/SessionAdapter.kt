package com.gon.kineapp.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gon.kineapp.R
import com.gon.kineapp.model.Patient
import com.gon.kineapp.model.Session
import com.gon.kineapp.utils.Utils
import kotlinx.android.synthetic.main.adapter_session.view.*

class SessionAdapter(private val sessions: MutableList<Session>, private val callback: SessionListener): RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {

    interface SessionListener {
        fun onSessionSelected(session: Session)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SessionViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.adapter_session, p0, false)
        return SessionViewHolder(v)
    }

    override fun getItemCount(): Int {
        return sessions.size
    }

    override fun onBindViewHolder(holder: SessionViewHolder, pos: Int) {
        holder.bind(sessions[pos], callback)
    }

    fun updateSession(session: Session) {
        val index = sessions.indexOfFirst { it.id == session.id }
        sessions.removeAt(index)
        sessions.add(index, session)
        notifyItemChanged(index)
    }

    fun addSession(session: Session) {
        sessions.add(0, session)
        notifyItemInserted(0)
    }

    class SessionViewHolder(private var viewItem: View): RecyclerView.ViewHolder(viewItem) {

        fun bind(session: Session, callback: SessionListener) {
            viewItem.tvDescription.text = if (!session.description.isEmpty()) session.description else viewItem.context.getString(R.string.observations_title)
            viewItem.tvDate.text = Utils.formatDate(session.date)
            viewItem.flContent.setOnClickListener {
                callback.onSessionSelected(session)
            }
        }

    }
}