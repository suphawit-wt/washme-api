package com.example.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class Branch(
    @Contextual
    @SerialName("_id")
    val id: Id<Branch> = newId(),
    val branchName: String,
)