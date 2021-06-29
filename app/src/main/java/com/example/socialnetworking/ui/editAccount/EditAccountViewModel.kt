package com.example.socialnetworking.ui.editAccount

import android.net.Uri
import android.provider.MediaStore
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialnetworking.data.models.AccountDetails
import com.example.socialnetworking.data.models.EditAccountModel
import com.example.socialnetworking.data.repository.Repository
import com.example.socialnetworking.utils.App
import com.example.socialnetworking.utils.Event
import kotlinx.coroutines.launch
import java.io.File

class EditAccountViewModel : ViewModel() {

    private val editAccountModel: EditAccountModel = EditAccountModel()
    private val selectedImage: MutableLiveData<File> = MutableLiveData()
    private val nameError: MutableLiveData<String> = MutableLiveData()
    private val bioError: MutableLiveData<String> = MutableLiveData()
    private val emailError: MutableLiveData<String> = MutableLiveData()
    private val stateLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val stateSuccess: MutableLiveData<Event<Boolean>> = MutableLiveData()
    private val stateError: MutableLiveData<Event<String>> = MutableLiveData()

    val selectedImageLiveData: LiveData<File> = selectedImage
    val nameErrorLiveData: LiveData<String> = nameError
    val bioErrorLiveData: LiveData<String> = bioError
    val emailErrorLiveData: LiveData<String> = emailError
    val stateLoadingLiveData: LiveData<Boolean> = stateLoading
    val stateErrorLiveData: LiveData<Event<String>> = stateError
    val stateSuccessLiveData: LiveData<Event<Boolean>> = stateSuccess
    val accountDetailsLiveData: LiveData<Event<AccountDetails>> =
        MutableLiveData(Event(Repository.accountDetailsLiveData.value))

    init {
        val accountDetails = Repository.accountDetailsLiveData.value
        editAccountModel.name = accountDetails?.name
        editAccountModel.email = accountDetails?.email
        editAccountModel.bio = accountDetails?.bio
    }

    fun changeName(newValue: String?) {
        editAccountModel.name = newValue

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
        editAccountModel.bio = newValue

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
        editAccountModel.email = newValue

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

    fun onGalleryIntentSuccess(selectedImageUri: Uri) {
        val projection = Array(1) { MediaStore.Images.Media.DATA }
        val cursor = App.context.contentResolver.query(
            selectedImageUri,
            projection,
            null,
            null,
            null
        )
        cursor?.moveToFirst()
        val imagePath = cursor?.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        cursor?.close()
        imagePath?.let { selectedImage.value = File(it) }
    }

    fun save() {
        if (!isValid()) return

        viewModelScope.launch {
            stateLoading.value = true
            try {
                Repository.editAccount(editAccountModel, selectedImage.value)
                stateSuccess.value = Event(true)
            } catch (exception: Exception) {
                stateError.value = Event(exception.message)
            }
            stateLoading.value = false
        }
    }

    private fun isValid(): Boolean {
        changeName(editAccountModel.name)
        changeEmail(editAccountModel.email)
        changeBio(editAccountModel.bio)
        return nameError.value.isNullOrEmpty() && bioError.value.isNullOrEmpty() && emailError.value.isNullOrEmpty()
    }
}