package com.example.todoplanner.repositories

import android.util.Log
import com.example.todoplanner.adapters.PriorityDataClass
import com.example.todoplanner.adapters.StatusDataClass
import com.example.todoplanner.adapters.TasksDataClass
import com.example.todoplanner.di.ApiClient

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import javax.inject.Inject

class ListOfTasksRepository @Inject constructor(
    private val service : ApiClient
) {
    suspend fun getTasks() : Flow<ArrayList<TasksDataClass>> {
        return flow{
            var data = ArrayList<TasksDataClass>()
            try{
                data = service.getAllTasks()
            } catch(ex: Exception) {
                Log.e("ListOfTasksRepository.kt", "Ошибка при получении задач:" +
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
                Log.e("ListOfTasksRepository.kt", "Ошибка при получении приоритетов:" +
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
                Log.e("ListOfTasksRepository.kt", "Ошибка при получении статусов:" +
                        ex.stackTraceToString())
            } finally {
                emit(data)
            }
        }
    }

    suspend fun updateRole(id : Long, role : Long) {
        service.updateRole(id, role)
    }
}