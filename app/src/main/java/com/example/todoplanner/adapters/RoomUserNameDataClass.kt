package com.example.todoplanner.adapters

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "username")
data class RoomUserNameDataClass(
    @PrimaryKey(autoGenerate = false)
    val id : Long,
    @ColumnInfo(name = "first_name")
    val first_name : String,
    @ColumnInfo(name = "middle_name")
    val middle_name : String,
    @ColumnInfo(name = "last_name")
    val last_name : String,
    @ColumnInfo(name = "auth_id")
    val auth_id : Long,
    @ColumnInfo(name = "role")
    val role_id : Long
)