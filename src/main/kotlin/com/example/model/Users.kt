package com.example.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class Users(
    @Contextual
    @SerialName("_id")
    val id: Id<Users> = newId(),
    val username: String? = null,
    val email: String? = null,
    val fullname: String? = null,
    val password: String? = null,
    val salt: String? = null,
    val isAdmin: Boolean? = false,
)