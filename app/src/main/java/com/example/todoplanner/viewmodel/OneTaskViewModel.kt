package com.example.todoplanner.viewmodel

import com.example.todoplanner.repositories.OneTaskRepository

import android.content.Context

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import com.example.todoplanner.adapters.PriorityDataClass
import com.example.todoplanner.adapters.StatusDataClass
import com.example.todoplanner.adapters.TasksDataClass
import com.example.todoplanner.adapters.UserNameDataClass

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import javax.inject.Named
import javax.inject.Inject

import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class OneTaskViewModel @Inject constructor(
    @Named("oneTaskRepository") private val repository : OneTaskRepository
) : ViewModel() {

    val tasksMetadata = MutableLiveData<TasksDataClass>()
    val statusMetadata = MutableLiveData<ArrayList<StatusDataClass>>()
    val priorityMetadata = MutableLiveData<ArrayList<PriorityDataClass>>()
    val usernameMetadata = MutableLiveData<UserNameDataClass>()

    fun getTask(context : Context) {
        viewModelScope.launch{
            repository.getStatus().collect{
                statusMetadata.value = it
            }
            repository.getPriority().collect{
                priorityMetadata.value = it
            }
            while(true){
                val id = context.getSharedPreferences("token", Context.MODE_PRIVATE).getLong("id", 0)
                repository.getTask(id).collect{ task ->
                    if(tasksMetadata.value != task){
                        tasksMetadata.value = task
                        repository.getUsername(task.owner_id).collect{ username ->
                            usernameMetadata.value = username
                        }
                    }
                }
                delay(1000)
            }
        }
    }

    fun deleteTask(id : Long) {
        viewModelScope.launch{
            repository.deleteTask(id)
        }
    }

    fun updateStatus(id : Long, status_id : Long, date : String) {
        viewModelScope.launch{
            repository.updateStatus(id, status_id, date)
        }
    }
}