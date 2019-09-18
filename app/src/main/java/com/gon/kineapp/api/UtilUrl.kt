package com.gon.kineapp.api

class UtilUrl {

    companion object {

        const val BASE_URL = "http://198.199.121.38/api/v1/"
        //const val BASE_URL = "https://kinesioapp.herokuapp.com/api/v1/"
        const val USER_EXISTS = "user_exists"
        const val REGISTER_USER = "registration"
        const val LOGIN = "login"
        const val PATIENTS = "patients"
        const val MEDICS = "medics"
        const val EXERCISES = "exercises"
        const val SESSIONS = "clinical_sessions_for_patient/{id}"
        const val CREATE_SESSION = "clinical_sessions"
        const val UPDATE_SESSION = "clinical_sessions/{id}"
        const val PUBLIC_VIDEOS = "public_videos"
        const val QUESTIONS = "secret_questions"
        const val VIEW_PHOTO = "image/{id}"
        const val GET_PHOTOS_BY_TAG = "image/{patientId}/{tag}"
        const val UPLOAD_PHOTO = "image"
        const val DELETE_PHOTO = "image/{id}"
        const val PATIENTS_DETAIL = PATIENTS + "/detail"

    }
}