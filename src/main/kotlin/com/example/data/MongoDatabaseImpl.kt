package com.example.data

import com.example.model.Branch
import com.example.model.Users
import com.example.model.WashingMachine
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.id.toId
import org.litote.kmongo.set
import org.litote.kmongo.setTo

class MongoDatabaseImpl(
    database: CoroutineDatabase
): MongoDatabaseInterface {

    private val usersCollection = database.getCollection<Users>()
    private val branchCollection = database.getCollection<Branch>()
    private val washCollection = database.getCollection<WashingMachine>()

    override suspend fun userCreate(user: Users): Boolean {
        return usersCollection.insertOne(user).wasAcknowledged()
    }

    override suspend fun userGetById(userId: ObjectId): Users? {
        return usersCollection.findOneById(userId)
    }

    override suspend fun userGetByUsername(username: String?): Users? {
        return usersCollection.findOne(filter = Users::username eq username)
    }

    override suspend fun userGetByEmail(email: String?): Users? {
        return usersCollection.findOne(filter = Users::email eq email)
    }

    override suspend fun userUpdate(userId: ObjectId, user: Users): Boolean {
        return  usersCollection.updateOne(
            filter = Users::id eq userId.toId(),
            update = set(
                Users::username setTo user.username,
                Users::email setTo user.email,
                Users::fullname setTo user.fullname,
                Users::password setTo user.password,
                Users::salt setTo user.salt
            )
        ).wasAcknowledged()
    }

    override suspend fun branchCreate(branch: Branch): Boolean {
        return branchCollection.insertOne(branch).wasAcknowledged()
    }

    override suspend fun branchGetAll(): List<Branch> {
        return branchCollection.find().toList()
    }

    override suspend fun branchGetById(branchId: ObjectId): Branch? {
        return branchCollection.findOneById(branchId)
    }

    override suspend fun branchUpdate(branchId: ObjectId, branch: Branch): Boolean {
        return branchCollection.updateOne(
            filter = Branch::id eq branchId.toId(),
            update = set(
                Branch::branchName setTo branch.branchName,
            )
        ).wasAcknowledged()
    }

    override suspend fun branchDelete(branchId: ObjectId): Boolean {
        return branchCollection.deleteOneById(branchId).wasAcknowledged()
    }

    override suspend fun washingMachineCreate(wash: WashingMachine): Boolean {
        return washCollection.insertOne(wash).wasAcknowledged()
    }

    override suspend fun washingMachineGetAll(): List<WashingMachine> {
        return washCollection.find().toList()
    }

    override suspend fun washingMachineGetById(washId: ObjectId): WashingMachine? {
        return washCollection.findOneById(washId)
    }

    override suspend fun washingMachineUpdate(washId: ObjectId, wash: WashingMachine): Boolean {
        return washCollection.updateOne(
            filter = WashingMachine::id eq washId.toId(),
            update = set(
                WashingMachine::washNo setTo wash.washNo,
                WashingMachine::price setTo wash.price,
                WashingMachine::size setTo wash.size,
                WashingMachine::time setTo wash.time,
                WashingMachine::type setTo wash.type,
                WashingMachine::status setTo wash.status,
                WashingMachine::branchId setTo wash.branchId,
            )
        ).wasAcknowledged()
    }

    override suspend fun washingMachineDelete(washId: ObjectId): Boolean {
        return washCollection.deleteOneById(washId).wasAcknowledged()
    }

}