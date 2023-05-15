package com.example.todoplanner.repositories

import android.util.Log
import com.example.todoplanner.adapters.PriorityDataClass
import com.example.todoplanner.adapters.TasksDataClass
import com.example.todoplanner.adapters.UserNameDataClass
import com.example.todoplanner.di.ApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddTaskRepository @Inject constructor(
    private val service : ApiClient
) {
    suspend fun addTask(task : TasksDataClass) : Flow<TasksDataClass> {
        return flow{
            var data = TasksDataClass(
                0,
                0,
                "2020-12-12",
                "2020-12-12",
                "0",
                "2020-12-12",
                0,
                "0",
                0,
                "0",
                0,
                0
            )
            try{
                data = service.createTask(task)
            } catch (ex : Exception) {
                Log.e("AddTaskRepository.kt", "Ошибка при создании задачи: " +
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
            } catch (ex : Exception) {
                Log.e("AddTaskRepository.kt", "Ошибка при получении приоритетов: " +
                        ex.stackTraceToString())
            } finally {
                emit(data)
            }
        }
    }

    suspend fun getEmployers() : Flow<ArrayList<UserNameDataClass>> {
        return flow{
            var data = ArrayList<UserNameDataClass>()
            try{
                data = service.getAllUsername()
            } catch (ex : Exception) {
                Log.e("AddTaskRepository.kt", "Ошибка при получении подчиненных: " +
                        ex.stackTraceToString())
            } finally {
                emit(data)
            }
        }
    }
}