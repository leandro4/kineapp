package com.gon.kineapp.api

import com.gon.kineapp.model.responses.LoginResponse
import com.gon.kineapp.model.responses.PatientListResponse
import com.gon.kineapp.model.responses.PublicVideosListResponse
import com.gon.kineapp.model.responses.SessionListResponse
import io.reactivex.Observable
import retrofit2.http.*

interface KinesApi {

    @GET(UtilUrl.LOGIN)
    fun login() : Observable<LoginResponse>

    @GET(UtilUrl.PATIENTS)
    fun getPatients(): Observable<PatientListResponse>

    @GET(UtilUrl.PATIENTS)
    fun getPublicVideos(): Observable<PublicVideosListResponse>

    @GET(UtilUrl.SESSIONS)
    fun getSessions(): Observable<SessionListResponse>
}