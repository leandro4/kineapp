package com.gon.kineapp.api

import com.gon.kineapp.model.ExercisesCalendar
import com.gon.kineapp.model.Photo
import com.gon.kineapp.model.Session
import com.gon.kineapp.model.Video
import com.gon.kineapp.model.requests.*
import com.gon.kineapp.model.responses.*
import com.gon.kineapp.utils.Authorization
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
        okHttpClient.addInterceptor {
            var request = it.request()

            if (requireHeader(request.url().uri().toString())) {
                val headers = request.headers().newBuilder().add("Authorization", "Token " + Authorization.getInstance().get()).build()
                request = request.newBuilder().headers(headers).build()
            }


            it.proceed(request)
        }
        return okHttpClient.build()
    }

    private fun requireHeader(url: String): Boolean {
        return !(url.contains("user_exists") || url.contains("login") || url.contains("registration"))
    }

    fun userExists(googleToken: String): Observable<UserExistsResponse> {
        val body = UserExistsBody(googleToken)
        return kinesApi.userExists(body)
    }

    fun registerUser(token: String, firstName: String, lastName: String, license: String?, number: String?, birthday: String?, email: String, questionId: Int, answer: String): Observable<UserRegisteredResponse> {
        val body = RegisterUserBody(token, firstName, lastName, questionId, license, number, birthday, email, answer)
        return kinesApi.registerUser(body)
    }

    fun checkAnswer(questionId: Int, answer: String, googleToken: String): Observable<LoginResponse> {
        val body = LoginBody(googleToken, questionId, answer)
        return kinesApi.login(body)
    }

    fun getPatientList(): Observable<PatientListResponse> {
        return kinesApi.getPatients()
    }

    fun getExercises(): Observable<ExercisesResponse> {

        val ex = listOf(ExercisesCalendar(0, "Hacer 3 series de 20 repeticiones de movimiento de codo derecho arriba y abajo", null),
            ExercisesCalendar(2, "Hacer 5 series de 15 repeticiones de movimiento de codo derecho arriba y abajo", null),
            ExercisesCalendar(4, "Hacer 7 series de 20 repeticiones de movimiento de codo derecho arriba y abajo", null),
            ExercisesCalendar(5, "Hacer 10 series de 20 repeticiones de movimiento de codo derecho arriba y abajo", null))
        val list = ExercisesResponse(ex.toMutableList())
        return Observable.just(list).delay(1000, TimeUnit.MILLISECONDS)

        //return kinesApi.getExercises()
    }

    fun getSessionList(id: String): Observable<SessionListResponse> {

/*        val photos = listOf(
            Photo("12322","https://i2.wp.com/saracossio.com/wp-content/uploads/2017/01/ander-frente-1.jpg?w=2000","frente"),
            Photo("22322","https://i2.wp.com/saracossio.com/wp-content/uploads/2017/01/andre-pixelada-lateral.jpg?w=2000", "derecha"),
            Photo("52322","https://i2.wp.com/saracossio.com/wp-content/uploads/2017/01/ander-frente-1.jpg?w=2000","dorso"),
            Photo("72322","https://i2.wp.com/saracossio.com/wp-content/uploads/2017/01/andre-pixelada-lateral.jpg?w=2000", "izquierda")
        )

        val list = listOf(
            Session("283", "9812", "17/08/2019", "Ya mueve casi con total normalidad la mano. Está pronto a darle el alta", photos.toMutableList()),
            Session("9827", "9812", "10/08/2019", "La fluidez es notoria. Quizás haya que empezar a usar peso en la mano", photos.toMutableList()),
            Session("5336", "9812", "03/08/2019", "Se reforzó la mano derecha, comienza a mover los dedos con cuidado", photos.toMutableList()),
            Session("643", "9812", "27/07/2019", "Fue un día de trabajo intenso. Estuvimos ejercitando la mano derecha que le cuesta mover los últimos 3 dedos", photos.toMutableList())
        )

        val response = SessionListResponse(list.toMutableList())

        return Observable.just(response).delay(1000, TimeUnit.MILLISECONDS)*/

        return kinesApi.getSessions(id)
    }

    fun createSession(patientId: String): Observable<Session> {
        return kinesApi.createSession(PatientIdBody(patientId))
    }

    fun updateSession(sessionId: String, description: String): Observable<Session> {
        return kinesApi.updateSession(sessionId, SessionDescriptionUpdateBody(description))
    }

    fun uploadPhoto(sessionId: String, content: String, tag: String): Observable<PhotoResponse> {
        return kinesApi.uploadPhoto(PhotoUploadBody(sessionId, content, tag))
    }

    fun deletePhoto(photoId: String): Observable<PhotoResponse> {
        return kinesApi.deletePhoto(photoId)
    }

    fun getPhoto(photoId: String): Observable<PhotoResponse> {
        return kinesApi.getPhoto(photoId)
    }

    fun getPublicVideos(): Observable<PublicVideosListResponse> {

        val videos = listOf(
            Video("8722", "Ejercicio de cuello", "https://resizer.iprofesional.com/unsafe/640x/https://assets.iprofesional.com/assets/jpg/2019/07/481410_landscape.jpg?3.5.0", "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4", true),
            Video("8122", "Cómo estirar las piernas", "https://www.wonderslist.com/wp-content/uploads/2016/07/Most-Beautiful-Girls-India.jpg", "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4", true),
            Video("8522", "Ejercicio para bajar la contractura de del omóplato derecho", "data:image/jpeg;base64,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", "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4", true),
            Video("8772", "Ejercicio de rodilla", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT-EpLTsZFUDN9Vf87gCnPgthUMR_6LUvkaYApCiMa633LZNRLq", "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4", true),
            Video("8782", "Otro ejercicio para la rodilla", "https://www.pointloma.edu/sites/default/files/styles/16_9_1200w/public/images/ATC.jpg?itok=pII3XLSJ", "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4", true),
            Video("8112", "Y un ejercicio para el pie", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAXat2LsnZ6uQbLHtIq0PLEzedF1fqftTSgrJtGidGEKl0YqM-", "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4", true)
        )

        val response = PublicVideosListResponse(videos.toMutableList())

        return Observable.just(response).delay(1000, TimeUnit.MILLISECONDS)

        //return kinesApi.getPublicVideos()
    }
}