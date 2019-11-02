package com.gon.kineapp.model.requests

import com.squareup.moshi.Json

data class ShareUnShareSessionsWith (@Json(name = "user_to_unshare_with") val unshareWith: String?, @Json(name = "user_to_share_with") val shareWith: String?)