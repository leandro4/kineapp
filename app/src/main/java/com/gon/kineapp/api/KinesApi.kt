package com.gon.kineapp.api

import com.gon.kineapp.model.responses.SomethingResponse
import io.reactivex.Observable
import retrofit2.http.*

interface KinesApi {

    @GET(UtilUrl.SOME)
    fun getSomething() : Observable<SomethingResponse>

}