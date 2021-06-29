package com.example.socialnetworking.ui.signUp

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialnetworking.data.models.SignUpModel
import com.example.socialnetworking.data.repository.AuthRepository
import com.example.socialnetworking.utils.Event
import com.example.socialnetworking.utils.InvalidRequestException
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SignUpViewModel : ViewModel() {

    private val signUpModel: SignUpModel = SignUpModel()
    private val stateLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val stateSuccess: MutableLiveData<Boolean> = MutableLiveData()
    private val stateError: MutableLiveData<Event<String>> = MutableLiveData()
    private val nameError: MutableLiveData<String> = MutableLiveData()
    private val bioError: MutableLiveData<String> = MutableLiveData()
    private val emailError: MutableLiveData<String> = MutableLiveData()
    private val passwordError: MutableLiveData<String> = MutableLiveData()
    private val confirmPasswordError: MutableLiveData<String> = MutableLiveData()

    val stateLoadingLiveData: LiveData<Boolean> = stateLoading
    val stateSuccessLiveData: LiveData<Boolean> = stateSuccess
    val stateErrorLiveData: LiveData<Event<String>> = stateError
    val nameErrorLiveData: LiveData<String> = nameError
    val bioErrorLiveData: LiveData<String> = bioError
    val emailErrorLiveData: LiveData<String> = emailError
    val passwordErrorLiveData: LiveData<String> = passwordError
    val confirmPasswordErrorLiveData: LiveData<String> = confirmPasswordError

    fun changeName(newValue: String?) {
        signUpModel.name = newValue

        when {
            newValue.isNullOrEmpty() -> {
                nameError.value = "Name should not empty"
            }

            (newValue.length < 2) -> {
                nameError.value = "Name should be at least 2 characters"
            }

            else -> {
                nameError.value = String()
            }
        }
    }

    fun changeBio(newValue: String?) {
        signUpModel.bio = newValue

        when {
            newValue.isNullOrEmpty() -> {
                bioError.value = "Bio should not empty"
            }

            else -> {
                bioError.value = String()
            }
        }
    }

    fun changeEmail(newValue: String?) {
        signUpModel.email = newValue

        when {
            newValue.isNullOrEmpty() -> {
                emailError.value = "Email should not empty"
            }

            (!Patterns.EMAIL_ADDRESS.matcher(newValue).matches()) -> {
                emailError.value = "Please enter a valid email address"
            }

            else -> {
                emailError.value = String()
            }
        }
    }

    fun changePassword(newValue: String?) {
        signUpModel.password = newValue

        when {
            newValue.isNullOrEmpty() -> {
                passwordError.value = "Password should not empty"
            }

            (newValue.length < 6) -> {
                passwordError.value = "Password should be at least 6 characters"
            }

            else -> {
                passwordError.value = String()
            }
        }

        if (newValue != signUpModel.confirmPassword) {
            confirmPasswordError.value = "Password mismatch"
        }
    }

    fun changeConfirmPassword(newValue: String?) {
        signUpModel.confirmPassword = newValue

        when {
            newValue.isNullOrEmpty() -> {
                confirmPasswordError.value = "Please confirm your password"
            }

            (newValue != signUpModel.password) -> {
                confirmPasswordError.value = "Password mismatch"
            }

            else -> {
                confirmPasswordError.value = String()
            }
        }
    }

    fun signUp() {
        if (!isValid()) return

        viewModelScope.launch {
            stateLoading.value = true
            try {
                AuthRepository.signUp(signUpModel)
                stateSuccess.value = true
            } catch (exception: InvalidRequestException) {
                stateError.value = Event("Email already taken")
            } catch (exception: Exception) {
                stateError.value = Event(exception.message)
            } finally {
                stateLoading.value = false
            }
        }
    }

    private fun isValid(): Boolean {
        changeName(signUpModel.name)
        changeEmail(signUpModel.email)
        changeBio(signUpModel.bio)
        changePassword(signUpModel.password)
        changeConfirmPassword(signUpModel.confirmPassword)
        return nameError.value.isNullOrEmpty() && bioError.value.isNullOrEmpty() && emailError.value.isNullOrEmpty() && passwordError.value.isNullOrEmpty() && confirmPasswordError.value.isNullOrEmpty()
    }
}