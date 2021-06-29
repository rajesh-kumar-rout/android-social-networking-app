package com.example.socialnetworking.ui.changePassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialnetworking.data.models.ChangePasswordModel
import com.example.socialnetworking.data.repository.AuthRepository
import com.example.socialnetworking.utils.Event
import com.example.socialnetworking.utils.InvalidRequestException
import kotlinx.coroutines.launch

class ChangePasswordViewModel: ViewModel() {

    private val changePasswordModel: ChangePasswordModel = ChangePasswordModel()
    private val stateLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val stateSuccess: MutableLiveData<Boolean> = MutableLiveData()
    private val stateError: MutableLiveData<Event<String>> = MutableLiveData()
    private val oldPasswordError: MutableLiveData<String> = MutableLiveData()
    private val newPasswordError: MutableLiveData<String> = MutableLiveData()
    private val confirmNewPasswordError: MutableLiveData<String> = MutableLiveData()

    val stateLoadingLiveData: LiveData<Boolean> = stateLoading
    val stateSuccessLiveData: LiveData<Boolean> = stateSuccess
    val stateErrorLiveData: LiveData<Event<String>> = stateError
    val oldPasswordErrorLiveData: LiveData<String> = oldPasswordError
    val newPasswordErrorLiveData: LiveData<String> = newPasswordError
    val confirmNewPasswordErrorLiveData: LiveData<String> = confirmNewPasswordError

    fun changeOldPassword(newValue: String) {
        changePasswordModel.oldPassword = newValue

        when {
            newValue.isEmpty() -> {
                oldPasswordError.value = "This field should not empty"
            }

            else -> {
                oldPasswordError.value = String()
            }
        }
    }

    fun changeNewPassword(newValue: String) {
        changePasswordModel.newPassword = newValue

        when {
            newValue.isEmpty() -> {
                newPasswordError.value = "This field should not empty"
            }

            (newValue.length < 6) -> {
                newPasswordError.value = "Password should be at least 6 characters"
            }

            else -> {
                newPasswordError.value = String()
            }
        }
    }

    fun changeConfirmNewPassword(newValue: String) {
        changePasswordModel.confirmNewPassword = newValue

        when {
            newValue.isEmpty() -> {
                confirmNewPasswordError.value = "Please confirm your password"
            }

            (newValue != changePasswordModel.newPassword) -> {
                confirmNewPasswordError.value = "Password mismatch"
            }

            else -> {
                confirmNewPasswordError.value = String()
            }
        }
    }

    fun changePassword() {
        if (!isValid()) return

        viewModelScope.launch {
            stateLoading.value = true
            try {
                AuthRepository.changePassword(changePasswordModel)
                stateSuccess.value = true
            } catch (exception: InvalidRequestException) {
                stateError.value = Event("Invalid Password")
            } catch (exception: Exception) {
                stateError.value = Event(exception.message)
            } finally {
                stateLoading.value = false
            }
        }
    }

    private fun isValid(): Boolean {
        changeOldPassword(changePasswordModel.oldPassword)
        changeNewPassword(changePasswordModel.newPassword)
        changeConfirmNewPassword(changePasswordModel.confirmNewPassword)
        return oldPasswordError.value.isNullOrEmpty() && newPasswordError.value.isNullOrEmpty() && confirmNewPasswordError.value.isNullOrEmpty()
    }
}