package com.gws.networking.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ServerEntity(
    val servername: String,
    val dbname: String,
    val username : String,
    val dbpassword : String
): Parcelable
