package com.example.todoplanner.room

import android.util.Log
import com.example.todoplanner.adapters.RoomUserNameDataClass

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val service : UsernameDao
) {
    suspend fun addUser(user : RoomUserNameDataClass){
                service.addUser(user)
    }

    suspend fun getUser() : Flow<RoomUserNameDataClass>{
        return flow{
            var data = RoomUserNameDataClass(0, "", "", "", 0,0)
            try{
                data = service.getUser()
            }
            catch(ex : Exception){
                Log.e("RoomRepository.kt", "Ошибка получения пользователя: " +
                        ex.stackTraceToString())
            }
            finally {
                emit(data)
            }
        }
    }

    suspend fun updateRole(role : Long){
        service.updateRole(role)
    }

    suspend fun deleteUser(user : RoomUserNameDataClass){
        service.delete(user)
    }
}