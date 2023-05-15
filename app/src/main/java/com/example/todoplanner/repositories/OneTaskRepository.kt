package com.example.todoplanner.repositories


import android.util.Log
import com.example.todoplanner.adapters.PriorityDataClass
import com.example.todoplanner.adapters.StatusDataClass
import com.example.todoplanner.adapters.TasksDataClass
import com.example.todoplanner.adapters.UserNameDataClass
import com.example.todoplanner.di.ApiClient

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import javax.inject.Inject

class OneTaskRepository @Inject constructor(
    private val service : ApiClient
) {
    suspend fun getTask(id : Long) : Flow<TasksDataClass> {
        return flow{
            var data = TasksDataClass(0, 0,"","","","",0,"",0,"",0,0)
            try{
                data = service.getTask(id)
            } catch(ex: Exception) {
                Log.e("OneTaskRepository.kt", "Ошибка при получении задачи:" +
                        ex.stackTraceToString())
            } finally {
                emit(data)
            }
        }
    }

    suspend fun getPriority() : Flow<ArrayList<PriorityDataClass>> {
        return flow{
            var data = ArrayList<PriorityDataClass>()
            try{
                data = service.getAllPriority()
            } catch(ex: Exception) {
                Log.e("OneTaskRepository.kt", "Ошибка при получении приоритетов:" +
                        ex.stackTraceToString())
            } finally {
                emit(data)
            }
        }
    }

    suspend fun getStatus() : Flow<ArrayList<StatusDataClass>> {
        return flow{
            var data = ArrayList<StatusDataClass>()
            try{
                data = service.getAllStatus()
            } catch(ex: Exception) {
                Log.e("OneTaskRepository.kt", "Ошибка при получении статусов:" +
                        ex.stackTraceToString())
            } finally {
                emit(data)
            }
        }
    }

    suspend fun getUsername(id : Long) : Flow<UserNameDataClass> {
        return flow{
            var data = UserNameDataClass(0, "", "", "", 0, 0)
            try{
                data = service.getUsername(id)
            } catch(ex : Exception) {
                Log.e("OneTaskRepository.kt", "Ошибка при получении данных пользователя:"
                        + ex.stackTraceToString())
            } finally {
                emit(data)
            }
        }
    }

    suspend fun deleteTask(id : Long){
        try {
            service.deleteTask(id)
        } catch (_ : Exception) { }
    }

    suspend fun updateStatus(id : Long, status_id : Long, date : String){
        try {
            service.updateStatus(id, status_id, date)
        } catch (_ : Exception) { }
    }
}