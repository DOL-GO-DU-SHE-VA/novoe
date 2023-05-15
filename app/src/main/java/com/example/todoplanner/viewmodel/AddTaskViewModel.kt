package com.example.todoplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoplanner.adapters.PriorityDataClass
import com.example.todoplanner.adapters.TasksDataClass
import com.example.todoplanner.adapters.UserNameDataClass
import com.example.todoplanner.repositories.AddTaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    @Named("addTaskRepository") private val repository : AddTaskRepository
) : ViewModel() {

    private val response = MutableLiveData<TasksDataClass>()
    val metadataPriority = MutableLiveData<ArrayList<PriorityDataClass>>()
    val metadataEmployers = MutableLiveData<ArrayList<UserNameDataClass>>()

    init {
        getPriority()
        getEmployers()
    }

    fun addTask(task : TasksDataClass) {
        viewModelScope.launch{
            repository.addTask(task).collect{
                response.value = it
            }
        }
    }

    private fun getPriority() {
        viewModelScope.launch{
            repository.getPriority().collect{
                metadataPriority.value = it
            }
        }
    }

    private fun getEmployers() {
        viewModelScope.launch{
            repository.getEmployers().collect{
                metadataEmployers.value = it
            }
        }
    }
}