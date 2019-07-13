package com.gon.kineapp.api

import com.gon.kineapp.model.Patient
import com.gon.kineapp.model.responses.LoginResponse
import com.gon.kineapp.model.responses.PatientListResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object KinesService {

    var kinesApi: KinesApi

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(UtilUrl.BASE_URL)
                .client(createOkHttpClientInterceptor())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        kinesApi = retrofit.create(KinesApi::class.java)
    }

    private fun createOkHttpClientInterceptor(): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpClient.addInterceptor(interceptor)
        return okHttpClient.build()
    }

    fun requestLogin(): Observable<LoginResponse> {
        return kinesApi.login()
    }

    fun getPatientList(): Observable<PatientListResponse> {


        val list = listOf(
            Patient("14", "female", "198281", "Gabriela", "Michoti", "1188776512", "gaby@gmail.com", "22678099")
            ,Patient("14", "female", "198281", "Sergio", "Massa", "1188776512", "gaby@gmail.com", "20723993")
            ,Patient("14", "female", "198281", "Elisa", "Carrió", "1188776512", "gaby@gmail.com", "16092776")
            ,Patient("14", "female", "198281", "Cristina", "Fernande", "1188776512", "gaby@gmail.com", "12098278")
            ,Patient("14", "female", "198281", "Nestor", "Quirchner", "1188776512", "gaby@gmail.com", "19098277"),
            Patient("14", "female", "198281", "Mauricio", "Macrisis", "1188776512", "gaby@gmail.com", "19443665")
        )

        val response = PatientListResponse(list.toMutableList())

        return Observable.just(response).delay(1500, TimeUnit.MILLISECONDS)

        //return kinesApi.getPatients()
    }
}