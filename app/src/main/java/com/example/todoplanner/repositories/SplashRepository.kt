package com.example.todoplanner.repositories

import com.example.todoplanner.adapters.ApiGetToken
import com.example.todoplanner.adapters.ApiPostToken
import com.example.todoplanner.di.ApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SplashRepository @Inject constructor(private val service : ApiClient) {
    suspend fun getToken(apt : ApiPostToken) : Flow<ApiGetToken>{
        return flow {
            var data = ApiGetToken("","",0,"","", arrayOf("").toList())
            try{
                data = service.getToken(apt)
            }
            catch (_: Exception){ }
            emit(data)
        }
    }
}