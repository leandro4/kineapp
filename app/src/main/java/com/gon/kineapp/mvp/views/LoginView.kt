package com.gon.kineapp.mvp.views

import com.gon.kineapp.model.Question
import com.gon.kineapp.model.User

interface LoginView: BaseView {

    fun onLoginSuccess()

    fun onLoginFailure()

    fun onUserRetrieved(myUser: User, questions: List<Question>)

    fun onUserDoesntExists()
}