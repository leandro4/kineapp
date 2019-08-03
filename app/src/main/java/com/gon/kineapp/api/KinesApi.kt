package com.gon.kineapp.api

import com.gon.kineapp.model.requests.LoginBody
import com.gon.kineapp.model.requests.RegisterUserBody
import com.gon.kineapp.model.requests.UserExistsBody
import com.gon.kineapp.model.responses.*
import io.reactivex.Observable
import retrofit2.http.*

interface KinesApi {

    @POST(UtilUrl.USER_EXISTS)
    fun userExists(@Body body: UserExistsBody): Observable<UserExistsResponse>

    @POST(UtilUrl.REGISTER_USER)
    fun registerUser(@Body body: RegisterUserBody): Observable<UserRegisteredResponse>

    @POST(UtilUrl.LOGIN)
    fun login(@Body body: LoginBody): Observable<LoginResponse>

    @GET(UtilUrl.PATIENTS)
    fun getPatients(): Observable<PatientListResponse>

    @GET(UtilUrl.PATIENTS)
    fun getPublicVideos(): Observable<PublicVideosListResponse>

    @GET(UtilUrl.SESSIONS)
    fun getSessions(): Observable<SessionListResponse>
}