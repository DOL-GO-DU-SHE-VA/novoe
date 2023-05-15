package com.example.todoplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoplanner.adapters.AuthDataClass
import com.example.todoplanner.adapters.UserNameDataClass
import com.example.todoplanner.repositories.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LoginViewModel @Inject constructor(@Named("LoginRepository") private val repository: LoginRepository) : ViewModel() {
    val authMetadata = MutableLiveData<AuthDataClass>()
    val usernameMetadata = MutableLiveData<UserNameDataClass>()

    fun auth(login : String, password : String) {
        viewModelScope.launch{
            repository.auth(login, password).collect{
                authMetadata.value = it
            }
        }
    }

    fun getUsername(id : Long) {
        viewModelScope.launch{
            repository.getUsername(id).collect{
                usernameMetadata.value = it
            }
        }
    }
}