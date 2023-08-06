package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val statusCode: Int,
    val token: String? = null,
    val user: Users? = null,
    val branchList: List<Branch>? = null,
    val branch: Branch? = null,
    val washList: List<WashingMachine>? = null,
    val wash: WashingMachine? = null,
)
