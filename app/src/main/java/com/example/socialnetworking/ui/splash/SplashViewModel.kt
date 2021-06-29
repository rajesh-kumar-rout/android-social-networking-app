package com.example.socialnetworking.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialnetworking.data.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val shouldNavigateLoginActivity: MutableLiveData<Boolean> = MutableLiveData()
    val shouldNavigateLoginActivityLiveData: LiveData<Boolean> = shouldNavigateLoginActivity

    fun decideLandingActivity() {
        viewModelScope.launch {
            delay(3000)
            shouldNavigateLoginActivity.value = !AuthRepository.isLoggedIn()
        }
    }
}