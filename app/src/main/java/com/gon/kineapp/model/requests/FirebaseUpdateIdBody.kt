package com.gon.kineapp.model.requests

import com.squareup.moshi.Json

data class FirebaseUpdateIdBody (@Json(name="firebase_device_id") val firebaseId: String)