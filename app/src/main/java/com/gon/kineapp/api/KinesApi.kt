package com.gon.kineapp.api

import com.gon.kineapp.model.responses.LoginResponse
import io.reactivex.Observable
import retrofit2.http.*

interface KinesApi {

    @GET(UtilUrl.LOGIN)
    fun login() : Observable<LoginResponse>

}