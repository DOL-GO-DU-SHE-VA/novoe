package com.example.todoplanner.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import com.example.todoplanner.adapters.RoomUserNameDataClass
import com.example.todoplanner.room.RoomRepository

import kotlinx.coroutines.launch

import javax.inject.Inject
import javax.inject.Named

import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class RoomViewModel @Inject constructor(
    @Named("roomRepository") private val repository : RoomRepository
) : ViewModel() {
    val userMetadata = MutableLiveData<RoomUserNameDataClass>()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch{
            repository.getUser().collect{
                userMetadata.value = it
            }
        }
    }

    fun saveUser(user : RoomUserNameDataClass) {
        viewModelScope.launch{
            repository.addUser(user)
        }
    }

    fun updateRole(role : Long) {
        viewModelScope.launch{
            repository.updateRole(role)
        }
    }

    fun delete(user : RoomUserNameDataClass){
        viewModelScope.launch {
            repository.deleteUser(user)
        }
    }
}