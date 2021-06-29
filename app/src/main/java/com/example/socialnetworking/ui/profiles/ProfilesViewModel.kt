package com.example.socialnetworking.ui.profiles

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialnetworking.data.models.PaginatedData
import com.example.socialnetworking.data.models.Profile
import com.example.socialnetworking.data.repository.Repository
import com.example.socialnetworking.utils.EXTRA_ACTION_FOLLOWER
import kotlinx.coroutines.launch

class ProfilesViewModel(
    private val action: String
) : ViewModel() {

    val paginatedProfiles: MutableLiveData<PaginatedData<Profile>> = MutableLiveData(PaginatedData())

    init {
        fetchProfiles()
    }

    fun fetchProfiles() {
        if (!paginatedProfiles.value!!.shouldFetch()) return

        paginatedProfiles.value = paginatedProfiles.value?.stateLoading()
        viewModelScope.launch {
            try {
                val profiles = if (action == EXTRA_ACTION_FOLLOWER) {
                    Repository.getFollowers(paginatedProfiles.value!!.data.size)
                } else {
                    Repository.getFollowings(paginatedProfiles.value!!.data.size)
                }
                paginatedProfiles.value = paginatedProfiles.value?.stateSuccess(profiles)
            } catch (exception: Exception) {
                paginatedProfiles.value = paginatedProfiles.value?.stateError(exception)
            }
        }
    }
}