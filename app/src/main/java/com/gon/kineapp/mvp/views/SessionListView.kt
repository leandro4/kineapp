package com.gon.kineapp.mvp.views

import com.gon.kineapp.model.Session

interface SessionListView: BaseView {

    fun onSessionsReceived(sessions: MutableList<Session>)

}