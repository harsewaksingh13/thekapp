package com.harsewak.kapp.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.harsewak.kapp.data.models.User
import com.harsewak.kapp.data.tables.Users

@Database(entities = [User::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun users(): Users

    companion object {
        fun create(context: Context): LocalDatabase {
            return Room.databaseBuilder(context, LocalDatabase::class.java, "app_db").build()
        }
    }

}