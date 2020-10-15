package com.harsewak.kapp.data.tables

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.harsewak.kapp.data.models.User
import io.reactivex.Flowable

interface BaseDao<T>{

    fun save(obj: T) : Long

    fun get(id: String): Flowable<T>

    fun delete(obj: T): Int

    fun getAll(): Flowable<List<T>>
}

open abstract class BaseDaoImpl<T> : BaseDao<T> {

    fun selectAll() : String {
        return ""
    }

}

@Dao()
abstract class Users : BaseDaoImpl<User>() {

    companion object {
        const val NAME = "users"
    }

    @Insert
    abstract override fun save(obj: User): Long

    @Query("SELECT * FROM $NAME WHERE id = :id")
    abstract override fun get(id: String): Flowable<User>

    @Delete
    abstract override fun delete(obj: User): Int

    @Query("SELECT * FROM $NAME")
    abstract override fun getAll(): Flowable<List<User>>
}

