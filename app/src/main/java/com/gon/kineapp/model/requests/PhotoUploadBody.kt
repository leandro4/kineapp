package com.gon.kineapp.model.requests

import com.squareup.moshi.Json

data class PhotoUploadBody(@Json(name="clinical_session_id") val sessionId: String, val content: String, val tag: String)