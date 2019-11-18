package com.gon.kineapp.api

import com.gon.kineapp.model.*
import com.gon.kineapp.model.requests.*
import com.gon.kineapp.model.responses.*
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.POST
import retrofit2.http.Multipart



interface KinesApi {

    @POST(UtilUrl.USER_EXISTS)
    fun userExists(@Body body: UserExistsBody): Observable<UserExistsResponse>

    @POST(UtilUrl.DEVICE_ID)
    fun updateFirebaseId(@Body body: FirebaseUpdateIdBody): Completable

    @POST(UtilUrl.REGISTER_USER)
    fun registerUser(@Body body: RegisterUserBody): Observable<UserRegisteredResponse>

    @POST(UtilUrl.VERIFY_SESSION)
    fun verifySession(@Body body: VerifySessionBody): Observable<LoginResponse>

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

    @GET(UtilUrl.SESSIONS)
    fun getSessions(@Path("id") id: String): Observable<SessionListResponse>

    @POST(UtilUrl.CREATE_SESSION)
    fun createSession(@Body id: PatientIdBody): Observable<Session>

    @PATCH(UtilUrl.UPDATE_SESSION)
    fun updateSession(@Path("id") id: String, @Body body: SessionDescriptionUpdateBody): Observable<Session>

    @DELETE(UtilUrl.DELETE_SESSION)
    fun deleteSession(@Path("id") id: String): Completable

    @POST(UtilUrl.UPLOAD_PHOTO)
    fun uploadPhoto(@Body body: PhotoUploadBody): Observable<PhotoResponse>

    @DELETE(UtilUrl.DELETE_PHOTO)
    fun deletePhoto(@Path("id") id: String): Completable

    @GET(UtilUrl.GET_PHOTOS_BY_TAG)
    fun getPhotosByTag(@Path("patientId") id: String, @Path("tag") tag: String): Observable<PhotosListResponse>

    @GET(UtilUrl.GET_PHOTOS_BY_SESSION)
    fun getPhotosBySession(@Path("session_id") id: String): Observable<PhotosListResponse>

    @GET(UtilUrl.VIEW_PHOTO)
    fun getPhoto(@Path("id") id: String): Observable<PhotoResponse>

    @GET(UtilUrl.MEDICS)
    fun getMedicList(): Observable<MedicListResponse>

    @PATCH(UtilUrl.PATIENTS_DETAIL)
    fun updateCurrentMedic(@Body body: UpdateMedicBody): Observable<User>

    @PATCH(UtilUrl.PATIENTS_DETAIL)
    fun updateUserThumbnail(@Body body: UpdateUserThumbnailBody): Observable<User>

    @GET(UtilUrl.CURRENT_PATIENT)
    fun getCurrentPatientUser(): Observable<User>

    @POST(UtilUrl.SHARE_SESSIONS)
    fun shareSessionToMedic(@Body body: ShareUnShareSessionsWith): Observable<SharedMedic>

    @POST(UtilUrl.UNSHARE_SESSIONS)
    fun unshareSessionWithMedic(@Body body: ShareUnShareSessionsWith): Observable<SharedMedic>

    @GET(UtilUrl.CURRENT_MEDIC)
    fun getCurrentMedicUser(): Observable<User>

    @Multipart
    @POST(UtilUrl.UPLOAD_VIDEO)
    fun uploadVideo(@Part content: MultipartBody.Part, @Part("name") videoName: RequestBody): Observable<Video>

    @DELETE(UtilUrl.DELETE_VIDEO)
    fun deleteVideo(@Path("id") id: String): Completable
}