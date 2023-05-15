package com.example.todoplanner.repositories

import android.util.Log
import com.example.todoplanner.adapters.PriorityDataClass
import com.example.todoplanner.adapters.TasksDataClass
import com.example.todoplanner.adapters.UserNameDataClass
import com.example.todoplanner.di.ApiClient

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import javax.inject.Inject

class EditTaskRepository @Inject constructor(
    private val service : ApiClient
) {
    suspend fun editTask(
        created_by: Long,
        date_of_creation: String,
        date_of_updater: String,
        description: String,
        deadline: String,
        id_for_who: Long,
        header: String,
        priority_id: Long,
        responsible: String,
        status_id: Long,
        owner_id: Long,
        id : Long
    ) {
        try{
            service.updateTask(
                created_by,
                date_of_creation,
                date_of_updater,
                description,
                deadline,
                id_for_who,
                header,
                priority_id,
                responsible,
                status_id,
                owner_id,
                id
            )
        } catch (ex : Exception) {
            Log.e("EditTaskRepository.kt", "Ошибка при обновлении задачи: " +
                    ex.stackTraceToString())
        }
    }

    suspend fun getTask(id : Long) : Flow<TasksDataClass> {
        return flow{
            var data = TasksDataClass(0, 0,"","","","",0,"",0,"",0,0)
            try{
                data = service.getTask(id)
            } catch(ex: Exception) {
                Log.e("EditTaskRepository.kt", "Ошибка при получении задачи:" +
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
                Log.e("EditTaskRepository.kt", "Ошибка при получении приоритетов:" +
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
                Log.e("EditTaskRepository.kt", "Ошибка при получении подчиненных: " +
                        ex.stackTraceToString())
            } finally {
                emit(data)
            }
        }
    }
}