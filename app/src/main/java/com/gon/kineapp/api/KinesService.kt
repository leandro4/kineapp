package com.gon.kineapp.api

import android.content.Context
import com.gon.kineapp.model.*
import com.gon.kineapp.model.requests.*
import com.gon.kineapp.model.responses.*
import com.gon.kineapp.utils.Authorization
import com.gon.kineapp.utils.MyUser
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import java.io.File


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
        okHttpClient.readTimeout(180, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(120, TimeUnit.SECONDS)
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

    fun verifySession(questionId: Int, answer: String): Observable<LoginResponse> {
        val body = VerifySessionBody(questionId, answer)
        return kinesApi.verifySession(body)
    }

    fun checkAnswer(questionId: Int, answer: String, googleToken: String): Observable<LoginResponse> {
        val body = LoginBody(googleToken, questionId, answer)
        return kinesApi.login(body)
    }

    fun getPatientList(): Observable<PatientListResponse> {
        return kinesApi.getPatients()
    }

    fun getSessionList(id: String): Observable<SessionListResponse> {
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

    fun deletePhoto(photoId: String): Completable {
        return kinesApi.deletePhoto(photoId)
    }

    fun getPhoto(photoId: String): Observable<PhotoResponse> {
        return kinesApi.getPhoto(photoId)
    }

    fun getPhotosByTag(patientId: String, tag: String): Observable<PhotosListResponse> {
        return kinesApi.getPhotosByTag(patientId, tag)
    }

    fun getMedicList(): Observable<MedicListResponse> {
        return kinesApi.getMedicList()
    }

    fun updateCurrentMedic(sharedMedic: SharedMedic): Observable<User> {
        return kinesApi.updateCurrentMedic(UpdateMedicBody(UpdateMedicBody.SharedMedicBody(sharedMedic)))
    }

    fun updateUserThumbnail(picture: String): Observable<User> {
        return kinesApi.updateUserThumbnail(UpdateUserThumbnailBody(picture))
    }

    fun createExercise(patientId: String, name: String, description: String, id: String?, day: ArrayList<Int>): Observable<ExercisesResponse> {
        val body = NewExerciseBody(patientId, name, description, id, day)
        return kinesApi.createExercise(body)
    }

    fun deleteExercise(id: String): Completable {
        return kinesApi.deleteExercise(id)
    }

    fun markAsDoneExercise(id: String): Observable<Exercise> {
        return kinesApi.markAsDoneExercise(id, DoneExerciseBody(true))
    }

    fun uploadVideo(filePath: String, videoName: String): Observable<Video> {
        val file = File(filePath)
        val requestBody = RequestBody.create(MediaType.parse("video/mp4"), file)
        val contentPart = MultipartBody.Part.createFormData("content", file.name, requestBody)

        val namePart = RequestBody.create(MediaType.parse("multipart/form-data"), videoName)

        return kinesApi.uploadVideo(contentPart, namePart)
    }

    fun deleteVideo(videoId: String): Completable {
        return kinesApi.deleteVideo(videoId)
    }

    fun syncCurrentUser(isMedic: Boolean, context: Context): Observable<User> {
        return getCurrentUserObs(isMedic).map { user ->
            MyUser.set(context, user)
            user
        }
    }

    private fun getCurrentUserObs(isMedic: Boolean): Observable<User> {
        return if (isMedic)
            kinesApi.getCurrentMedicUser()
        else kinesApi.getCurrentPatientUser()
    }
}