package com.example.todoplanner.repositories

import android.util.Log
import com.example.todoplanner.adapters.AuthDataClass
import com.example.todoplanner.adapters.UserNameDataClass
import com.example.todoplanner.di.ApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpRepository @Inject constructor(
    private val service : ApiClient
) {
    suspend fun regAuth(metadata : AuthDataClass) : Flow<AuthDataClass> {
        return flow{
            var data = AuthDataClass(0, "", "")
            try{
                data = service.regAuth(metadata)
            } catch(ex : Exception) {
                Log.e("SignUpRepository.kt", "Ошибка при регистрации auth:" +
                        ex.stackTraceToString())
            } finally {
                emit(data)
            }
        }
    }

    suspend fun regUsername(metadata : UserNameDataClass) : Flow<UserNameDataClass> {
        return flow{
            var data = UserNameDataClass(0, "", "", "",
                0, 0)
            try{
                data = service.regUsername(metadata)
            } catch(ex : Exception) {
                Log.e("SignUpRepository.kt", "Ошибка при регистрации username:" +
                        ex.stackTraceToString())
            } finally {
                emit(data)
            }
        }
    }
}