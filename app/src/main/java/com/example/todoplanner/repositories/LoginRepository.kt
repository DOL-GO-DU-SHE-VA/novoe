package com.example.todoplanner.repositories

import android.util.Log
import com.example.todoplanner.adapters.AuthDataClass
import com.example.todoplanner.adapters.UserNameDataClass
import com.example.todoplanner.di.ApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepository @Inject constructor(private val service : ApiClient) {

    suspend fun auth(login : String, password : String) : Flow<AuthDataClass> {
        return flow{
            var data = AuthDataClass(0, "", "")
            try{
                data = service.login(login, password)
            } catch(ex : Exception) {
                Log.e("LoginRepository.kt", "Ошибка при авторизации: " +
                        ex.stackTraceToString())
            } finally {
                emit(data)
            }
        }
    }

    suspend fun getUsername(id : Long) : Flow<UserNameDataClass> {
        return flow{
            var data = UserNameDataClass(
                0,
                "",
                "",
                "",
                0,
                0
            )
            try{
                data = service.getUsernameFromAuth(id)
            } catch(ex : Exception) {
                Log.e("LoginRepository.kt", "Ошибка при получении username: " +
                        ex.stackTraceToString())
            } finally {
                emit(data)
            }
        }
    }
}