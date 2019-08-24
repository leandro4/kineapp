package com.gon.kineapp.api

class UtilUrl {

    companion object {

        const val BASE_URL = "http://192.168.0.55/api/v1/"
        //const val BASE_URL = "https://kinesioapp.herokuapp.com/api/v1/"
        const val USER_EXISTS = "user_exists"
        const val REGISTER_USER = "registration"
        const val LOGIN = "login"
        const val PATIENTS = "patients"
        const val EXERCISES = "exercises"
        const val SESSIONS = "clinical_sessions_for_patient/{id}"
        const val CREATE_SESSION = "clinical_sessions"
        const val UPDATE_SESSION = "clinical_sessions/{id}"
        const val PUBLIC_VIDEOS = "public_videos"
        const val QUESTIONS = "secret_questions"
        const val VIEW_PHOTO = "image/{id}"
        const val UPLOAD_PHOTO = "image"
        const val DELETE_PHOTO = "image/{id}"

    }
}