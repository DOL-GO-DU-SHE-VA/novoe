package com.example.todoplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoplanner.adapters.AuthDataClass
import com.example.todoplanner.adapters.UserNameDataClass
import com.example.todoplanner.repositories.SignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SignUpViewModel @Inject constructor(
    @Named("SignUpRepository") private val repository : SignUpRepository
) : ViewModel() {

    val authMetadata = MutableLiveData<AuthDataClass>()
    val usernameMetadata = MutableLiveData<UserNameDataClass>()

    fun regAuth(metadata : AuthDataClass) {
        viewModelScope.launch{
            repository.regAuth(metadata).collect{
                authMetadata.value = it
            }
        }
    }

    fun regUsername(metadata : UserNameDataClass) {
        viewModelScope.launch{
            repository.regUsername(metadata).collect{
                usernameMetadata.value = it
            }
        }
    }
}