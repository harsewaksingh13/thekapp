package com.harsewak.kapp.data.tables

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.harsewak.kapp.data.models.User
import io.reactivex.Flowable

@Dao()
interface Users {

    companion object {
        const val NAME = "users"
    }

    @Insert
    fun save(user: User): Long

    @Suppress("AndroidUnresolvedRoomSqlReference")
    @Query("SELECT * FROM $NAME WHERE id = :id")
    fun get(id: String): Flowable<User>

    @Delete
    fun delete(user: User): Int
}

