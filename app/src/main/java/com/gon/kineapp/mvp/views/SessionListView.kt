package com.gon.kineapp.mvp.views

import com.gon.kineapp.model.Photo
import com.gon.kineapp.model.Session

interface SessionListView: BaseView {

    fun onSessionsReceived(sessions: MutableList<Session>)
    fun onSessionCreated(session: Session)
    fun onPhotosByTagReceived(photos: ArrayList<Photo>)

}