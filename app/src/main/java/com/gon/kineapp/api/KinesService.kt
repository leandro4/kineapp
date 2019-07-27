package com.gon.kineapp.api

import com.gon.kineapp.model.Patient
import com.gon.kineapp.model.Photo
import com.gon.kineapp.model.Session
import com.gon.kineapp.model.responses.LoginResponse
import com.gon.kineapp.model.responses.PatientListResponse
import com.gon.kineapp.model.responses.SessionListResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object KinesService {

    private var kinesApi: KinesApi

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

    fun getSessionList(): Observable<SessionListResponse> {

        val photos = listOf(
            Photo("12322","https://i2.wp.com/saracossio.com/wp-content/uploads/2017/01/ander-frente-1.jpg?w=2000", "https://i2.wp.com/saracossio.com/wp-content/uploads/2017/01/ander-frente-1.jpg?w=2000", "frente"),
            Photo("22322","https://i2.wp.com/saracossio.com/wp-content/uploads/2017/01/andre-pixelada-lateral.jpg?w=2000", "https://i2.wp.com/saracossio.com/wp-content/uploads/2017/01/andre-pixelada-lateral.jpg?w=2000", "derecha"),
            Photo("52322","https://i2.wp.com/saracossio.com/wp-content/uploads/2017/01/ander-frente-1.jpg?w=2000", "https://i2.wp.com/saracossio.com/wp-content/uploads/2017/01/ander-frente-1.jpg?w=2000", "dorso"),
            Photo("72322","https://i2.wp.com/saracossio.com/wp-content/uploads/2017/01/andre-pixelada-lateral.jpg?w=2000", "https://i2.wp.com/saracossio.com/wp-content/uploads/2017/01/andre-pixelada-lateral.jpg?w=2000", "izquierda")
        )

        val list = listOf(
            Session("Cachito", "27/07/2019", "Fue un día de trabajo intenso. Estuvimos ejercitando la mano derecha que le cuesta mover los últimos 3 dedos", photos.toMutableList()),
            Session("Cachito", "03/08/2019", "Se reforzó la mano derecha, comienza a mover los dedos con cuidado", photos.toMutableList()),
            Session("Cachito", "10/08/2019", "La fluidez es notoria. Quizás haya que empezar a usar peso en la mano", photos.toMutableList()),
            Session("Cachito", "17/08/2019", "Ya mueve casi con total normalidad la mano. Está pronto a darle el alta", photos.toMutableList())
        )

        val response = SessionListResponse(list.toMutableList())

        return Observable.just(response).delay(1000, TimeUnit.MILLISECONDS)

        //return kinesApi.getPatients()
    }

    //fun getQuestions():
}