package com.example.socialnetworking.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialnetworking.data.models.LoginModel
import com.example.socialnetworking.data.repository.AuthRepository
import com.example.socialnetworking.utils.Event
import com.example.socialnetworking.utils.UnAuthenticatedException
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val loginModel: LoginModel = LoginModel()

    private val stateLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val stateSuccess: MutableLiveData<Boolean> = MutableLiveData()
    private val stateError: MutableLiveData<Event<String>> = MutableLiveData()
    private val emailError: MutableLiveData<String> = MutableLiveData()
    private val passwordError: MutableLiveData<String> = MutableLiveData()

    val emailErrorLiveData: LiveData<String> = emailError
    val passwordErrorLiveData: LiveData<String> = passwordError
    val stateLoadingLiveData: LiveData<Boolean> = stateLoading
    val stateSuccessLiveData: LiveData<Boolean> = stateSuccess
    val stateErrorLiveData: LiveData<Event<String>> = stateError

    fun changeEmail(newValue: String?) {
        loginModel.email = newValue

        if (newValue.isNullOrEmpty()) {
            emailError.value = "Email should not empty"
        } else {
            emailError.value = String()
        }
    }

    fun changePassword(newValue: String?) {
        loginModel.password = newValue

        if (newValue.isNullOrEmpty()) {
            passwordError.value = "Password should not empty"
        } else {
            passwordError.value = String()
        }
    }

    fun login() {
        if (!isValid()) return

        viewModelScope.launch {
            stateLoading.value = true
            try {
                AuthRepository.login(loginModel)
                stateSuccess.value = true
            } catch (exception: UnAuthenticatedException) {
                stateError.value = Event("Invalid email or password")
            } catch (exception: Exception) {
                stateError.value = Event(exception.message)
            }
            stateLoading.value = false
        }
    }

    private fun isValid(): Boolean {
        changeEmail(loginModel.email)
        changePassword(loginModel.password)

        return emailError.value.isNullOrEmpty() && passwordError.value.isNullOrEmpty()
    }
}