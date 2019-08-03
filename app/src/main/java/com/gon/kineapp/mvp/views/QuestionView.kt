package com.gon.kineapp.mvp.views

interface QuestionView: BaseView {

    fun onCheckedAnswer()

    fun onAnswerInvalid()

    fun onAttemptsLimit()

}