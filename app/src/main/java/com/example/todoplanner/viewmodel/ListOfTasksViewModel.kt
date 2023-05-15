package com.example.todoplanner.viewmodel

import com.example.todoplanner.repositories.ListOfTasksRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import com.example.todoplanner.adapters.PriorityDataClass
import com.example.todoplanner.adapters.StatusDataClass
import com.example.todoplanner.adapters.TasksDataClass

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import javax.inject.Named
import javax.inject.Inject

import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class ListOfTasksViewModel @Inject constructor(
    @Named("listRepository") private val repository : ListOfTasksRepository
) : ViewModel() {

    val tasksMetadata = MutableLiveData<ArrayList<TasksDataClass>>()
    val statusMetadata = MutableLiveData<ArrayList<StatusDataClass>>()
    val priorityMetadata = MutableLiveData<ArrayList<PriorityDataClass>>()

    init {
        getTasks()
    }

    private fun getTasks() {
        viewModelScope.launch{
            repository.getStatus().collect{
                statusMetadata.value = it
            }
            repository.getPriority().collect{
                priorityMetadata.value = it
            }
            while(true){
                repository.getTasks().collect{
                    if(tasksMetadata.value != it){
                        tasksMetadata.value = it
                    }
                }
                delay(1000)
            }
        }
    }

    fun updateRole(id : Long, role : Long) {
        viewModelScope.launch{
            repository.updateRole(id, role)
        }
    }
}