package com.gon.kineapp.api

import com.gon.kineapp.model.responses.LoginResponse
import com.gon.kineapp.model.responses.PatientListResponse
import io.reactivex.Observable
import retrofit2.http.*

interface KinesApi {

    @GET(UtilUrl.LOGIN)
    fun login() : Observable<LoginResponse>

    @GET(UtilUrl.PATIENTS)
    fun getPatients(): Observable<PatientListResponse>
}