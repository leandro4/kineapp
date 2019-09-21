package com.gon.kineapp.api

import com.gon.kineapp.model.Exercise
import com.gon.kineapp.model.Session
import com.gon.kineapp.model.User
import com.gon.kineapp.model.requests.*
import com.gon.kineapp.model.responses.*
import io.reactivex.Completable
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

    @POST(UtilUrl.EXERCISE)
    fun createExercise(@Body ex: NewExerciseBody): Observable<ExercisesResponse>

    @DELETE(UtilUrl.EXERCISE_ROUTE)
    fun deleteExercise(@Path("id") id: String): Completable

    @PATCH(UtilUrl.EXERCISE_ROUTE)
    fun markAsDoneExercise(@Path("id") id: String, @Body body: DoneExerciseBody): Observable<Exercise>

    @GET(UtilUrl.PATIENTS)
    fun getPublicVideos(): Observable<PublicVideosListResponse>

    @GET(UtilUrl.SESSIONS)
    fun getSessions(@Path("id") id: String): Observable<SessionListResponse>

    @POST(UtilUrl.CREATE_SESSION)
    fun createSession(@Body id: PatientIdBody): Observable<Session>

    @PATCH(UtilUrl.UPDATE_SESSION)
    fun updateSession(@Path("id") id: String, @Body body: SessionDescriptionUpdateBody): Observable<Session>

    @POST(UtilUrl.UPLOAD_PHOTO)
    fun uploadPhoto(@Body body: PhotoUploadBody): Observable<PhotoResponse>

    @DELETE(UtilUrl.DELETE_PHOTO)
    fun deletePhoto(@Path("id") id: String): Completable

    @GET(UtilUrl.GET_PHOTOS_BY_TAG)
    fun getPhotosByTag(@Path("patientId") id: String, @Path("tag") tag: String): Observable<PhotosListResponse>

    @GET(UtilUrl.VIEW_PHOTO)
    fun getPhoto(@Path("id") id: String): Observable<PhotoResponse>

    @GET(UtilUrl.MEDICS)
    fun getMedicList(): Observable<MedicListResponse>

    @PATCH(UtilUrl.PATIENTS_DETAIL)
    fun updateCurrentMedic(@Body body: UpdateMedicBody): Observable<User>
}