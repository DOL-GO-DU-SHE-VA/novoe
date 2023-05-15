package com.example.todoplanner.room

import androidx.room.*
import com.example.todoplanner.adapters.RoomUserNameDataClass

@Dao
interface UsernameDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user : RoomUserNameDataClass)

    @Query("SELECT * FROM username")
    suspend fun getUser() : RoomUserNameDataClass

    @Query("UPDATE username SET role=:role")
    suspend fun updateRole(role : Long)

    @Query(
        "UPDATE username SET" +
                " id=:id," +
                " first_name=:first_name," +
                " middle_name=:middle_name," +
                " last_name=:last_name," +
                " auth_id=:auth_id," +
                " role=:role"
    )
    suspend fun updateUser(
        id : Long,
        first_name : String,
        middle_name : String,
        last_name : String,
        auth_id : Long,
        role : Long
    )

    @Delete
    suspend fun delete(user : RoomUserNameDataClass)
}