package com.example.todoplanner.viewmodel

import com.example.todoplanner.repositories.EditTaskRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import com.example.todoplanner.adapters.PriorityDataClass
import com.example.todoplanner.adapters.TasksDataClass
import com.example.todoplanner.adapters.UserNameDataClass

import javax.inject.Inject
import javax.inject.Named

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    @Named("editTaskRepository") private val repository: EditTaskRepository
) : ViewModel() {

    val tasksMetadata = MutableLiveData<TasksDataClass>()
    val metadataPriority = MutableLiveData<ArrayList<PriorityDataClass>>()
    val metadataEmployers = MutableLiveData<ArrayList<UserNameDataClass>>()

    init {
        getPriority()
        getEmployers()
    }

    fun editTask(
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
        viewModelScope.launch{
            repository.editTask(
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
        }
    }

    fun getTask(id : Long) {
        viewModelScope.launch{
            while(true){
                repository.getTask(id).collect{ task ->
                    if(tasksMetadata.value != task){
                        tasksMetadata.value = task
                    }
                }
                delay(1000)
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