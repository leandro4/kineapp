package com.gon.kineapp.api

import com.gon.kineapp.model.responses.SomethingResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

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

    fun getSomething(): Observable<SomethingResponse> {
        return kinesApi.getSomething()
    }

}