package com.gon.kineapp.utils

import android.content.Context
import com.gon.kineapp.model.Question
import com.gon.kineapp.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object QuestionsList {

    private const val PREFIX = "questions_list"
    private const val QUESTIONS_KEY = "questions"

    fun get(context: Context): List<Question> {
        val questionsString = SharedPreferencesEditor(context, PREFIX).valueForKey(QUESTIONS_KEY)
        return  if (questionsString != null && !questionsString.isEmpty())
                     Gson().fromJson(questionsString, Questions::class.java).questions
                else ArrayList()
    }

    fun set(context: Context, questions: List<Question>) {
        val list = Questions(questions)
        val questionsString = Gson().toJson(list)
        SharedPreferencesEditor(context, PREFIX).setValueForKey(QUESTIONS_KEY, questionsString)
    }

    class Questions (var questions: List<Question>)
}