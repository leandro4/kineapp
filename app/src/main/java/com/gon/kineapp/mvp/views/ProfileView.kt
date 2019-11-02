package com.gon.kineapp.mvp.views

import com.gon.kineapp.model.SharedMedic
import com.gon.kineapp.model.User

interface ProfileView : BaseView {

    fun onMedicListResponse(medics: List<User>, isForMainMedic: Boolean)
    fun onMedicUpdated(user: User)
    fun onSharedMedicUpdated(sharedMedic: SharedMedic)
    fun onPhotoEdited()

}