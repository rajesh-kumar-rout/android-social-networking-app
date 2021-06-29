package com.example.socialnetworking.ui.addPost

import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.lifecycle.*
import com.example.socialnetworking.data.repository.Repository
import com.example.socialnetworking.utils.ALLOWED_IMAGE_TYPE
import com.example.socialnetworking.utils.App
import com.example.socialnetworking.utils.Event
import com.example.socialnetworking.utils.FILE_PROVIDER_AUTHORITY
import kotlinx.coroutines.launch
import java.io.File

class AddPostViewModel : ViewModel() {

    private val stateLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val stateError: MutableLiveData<Event<String>> = MutableLiveData()
    private val stateSuccess: MutableLiveData<Event<Boolean>> = MutableLiveData()
    private val selectedImage: MutableLiveData<File> = MutableLiveData()
    private val pickImageFromGallery: MutableLiveData<Event<Boolean>> = MutableLiveData()
    private val pickImageFromCamera: MutableLiveData<Event<Uri>> = MutableLiveData()

    val stateLoadingLiveData: LiveData<Boolean> = stateLoading
    val stateErrorLiveData: LiveData<Event<String>> = stateError
    val stateSuccessLiveData: LiveData<Event<Boolean>> = stateSuccess
    val selectedImageLiveData: LiveData<File> = selectedImage
    val pickImageFromGalleryLiveData: LiveData<Event<Boolean>> = pickImageFromGallery
    val pickImageFromCameraLiveData: LiveData<Event<Uri>> = pickImageFromCamera

    private lateinit var tempImageFile: File

    fun pickImageFromGallery() {
        pickImageFromGallery.value = Event(true)
    }

    fun pickImageFromCamera() {
        tempImageFile = File.createTempFile(
            System.currentTimeMillis().toString(),
            ALLOWED_IMAGE_TYPE,
            App.context.cacheDir
        )
        val imageUri = FileProvider.getUriForFile(
            App.context,
            FILE_PROVIDER_AUTHORITY,
            tempImageFile
        )
        pickImageFromCamera.value = Event(imageUri)
    }

    fun onCameraIntentSuccess() {
        selectedImage.value = tempImageFile
    }

    fun onCameraIntentCanceled() {
        tempImageFile.delete()
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
        imagePath?.let { selectedImage.value = File(imagePath) }
    }

    fun upload(description: String) {
        if (selectedImage.value == null) return

        viewModelScope.launch {
            stateLoading.value = true
            try {
                Repository.createPost(selectedImage.value!!, description)
                stateSuccess.value = Event(true)
            } catch (exception: Exception) {
                stateError.value = Event(exception.message)
            }
            stateLoading.value = false
        }
    }
}