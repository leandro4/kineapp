package com.gon.kineapp.utils

import android.content.Context
import com.gon.kineapp.model.User
import com.google.gson.Gson

object MyUser {

    private const val PREFIX = "my_user"
    private const val MY_USER_KEY = "my_user_key"

    fun get(context: Context): User? {
        val userString = SharedPreferencesEditor(context, PREFIX).valueForKey(MY_USER_KEY)
        return  if (userString != null && !userString.isEmpty())
                     Gson().fromJson(userString, User::class.java)
                else null
    }

    fun set(context: Context, user: User?) {
        val userString = Gson().toJson(user)
        SharedPreferencesEditor(context, PREFIX).setValueForKey(MY_USER_KEY, userString)
    }

}