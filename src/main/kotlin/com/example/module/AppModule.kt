package com.example.module

import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import com.example.data.MongoDatabaseImpl
import com.example.data.MongoDatabaseInterface

val appModule = module {
    single {
        KMongo.createClient()
            .coroutine
            .getDatabase("WashMeDB")
    }
    single<MongoDatabaseInterface> {
        MongoDatabaseImpl(get())
    }
}