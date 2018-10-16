package com.harsewak.kapp.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.harsewak.kapp.data.tables.Users

@Entity(tableName = Users.NAME)
data class User(@PrimaryKey var id : String, var email : String)