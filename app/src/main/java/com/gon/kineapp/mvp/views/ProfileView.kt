package com.gon.kineapp.mvp.views

import com.gon.kineapp.model.User

interface ProfileView : BaseView {

    fun onMedicListResponse(medics: List<User>)
    fun onMedicUpdated(user: User)
    fun onPhotoEdited()

}