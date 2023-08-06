package com.example.data

import com.example.model.Branch
import com.example.model.Users
import com.example.model.WashingMachine
import org.bson.types.ObjectId

interface MongoDatabaseInterface {
    suspend fun userCreate(user: Users): Boolean
    suspend fun userGetById(userId: ObjectId): Users?
    suspend fun userGetByUsername(username: String?): Users?
    suspend fun userGetByEmail(email: String?): Users?
    suspend fun userUpdate(userId: ObjectId, user: Users): Boolean
    suspend fun branchCreate(branch: Branch): Boolean
    suspend fun branchGetAll(): List<Branch>
    suspend fun branchGetById(branchId: ObjectId): Branch?
    suspend fun branchUpdate(branchId: ObjectId, branch: Branch): Boolean
    suspend fun branchDelete(branchId: ObjectId): Boolean
    suspend fun washingMachineCreate(wash: WashingMachine): Boolean
    suspend fun washingMachineGetAll(): List<WashingMachine>
    suspend fun washingMachineGetById(washId: ObjectId): WashingMachine?
    suspend fun washingMachineUpdate(washId: ObjectId, wash: WashingMachine): Boolean
    suspend fun washingMachineDelete(washId: ObjectId): Boolean
}