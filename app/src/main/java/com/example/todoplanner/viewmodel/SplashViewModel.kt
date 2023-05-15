package com.example.todoplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoplanner.adapters.ApiGetToken
import com.example.todoplanner.adapters.ApiPostToken
import com.example.todoplanner.repositories.SplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SplashViewModel @Inject constructor(@Named("SplashRepository") private val repository: SplashRepository) : ViewModel() {

    val token = MutableLiveData<ApiGetToken>()

    init {
        getToken(ApiPostToken("admin", "123456"))
    }

    private fun getToken(apiPostToken: ApiPostToken){
        viewModelScope.launch {
            repository.getToken(apiPostToken).collect{
                token.value = it
            }
        }
    }

}