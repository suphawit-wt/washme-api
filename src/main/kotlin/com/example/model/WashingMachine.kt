package com.example.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class WashingMachine(
    @Contextual
    @SerialName("_id")
    val id: Id<WashingMachine> = newId(),
    val washNo: String,
    val price: Int,
    val size: Int,
    val time: Int,
    val type: String,
    val status: String,
    val branchId: String,
)
