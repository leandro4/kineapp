package com.gon.kineapp.mvp.views

import com.gon.kineapp.model.Question
import com.gon.kineapp.model.User

interface LoginView: BaseView {

    fun onUserRetrieved(myUser: User, questions: List<Question>)

    fun onUserDoesntExists(questions: List<Question>)

    fun onUserCreated(myUser: User)
}