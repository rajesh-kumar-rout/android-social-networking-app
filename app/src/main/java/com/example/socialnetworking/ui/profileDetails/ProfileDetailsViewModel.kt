package com.example.socialnetworking.ui.profileDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialnetworking.data.models.ProfileDetails
import com.example.socialnetworking.data.repository.Repository
import com.example.socialnetworking.utils.Event
import kotlinx.coroutines.launch

class ProfileDetailsViewModel(
    private val profileId: Int
) : ViewModel() {

    private val profileDetails: MutableLiveData<ProfileDetails> = MutableLiveData()
    private val stateDetailsLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val stateDetailsError: MutableLiveData<String> = MutableLiveData()
    private val stateLoading = MutableLiveData<Boolean>()
    private val stateError = MutableLiveData<Event<String>>()

    val profileDetailsLiveData: LiveData<ProfileDetails> = profileDetails
    val stateDetailsLoadingLiveData: LiveData<Boolean> = stateDetailsLoading
    val stateDetailsErrorLiveData: LiveData<String> = stateDetailsError
    val stateLoadingLiveData: LiveData<Boolean> = stateLoading
    val stateErrorLiveData: LiveData<Event<String>> = stateError

    init {
        fetchProfileDetails()
    }

    fun fetchProfileDetails() {
        viewModelScope.launch {
            stateDetailsLoading.value = true
            try {
                profileDetails.value = Repository.getProfileDetails(profileId)
            } catch (exception: Exception) {
                stateDetailsError.value = exception.message
            }
            stateDetailsLoading.value = false
        }
    }

    fun follow() {
        val details = profileDetails.value!!

        viewModelScope.launch {
            stateLoading.value = true
            try {
                details.isFollowing = if (details.isFollowing) {
                    Repository.unFollow(details.id)
                    false
                } else {
                    Repository.follow(details.id)
                    true
                }
                profileDetails.value = profileDetails.value
            } catch (exception: Exception) {
                stateError.value = Event(exception.message)
            }
            stateLoading.value = false
        }
    }
}



