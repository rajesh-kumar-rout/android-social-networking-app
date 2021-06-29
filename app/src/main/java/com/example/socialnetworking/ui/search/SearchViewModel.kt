package com.example.socialnetworking.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialnetworking.data.models.NetworkStatus
import com.example.socialnetworking.data.models.PaginatedData
import com.example.socialnetworking.data.models.Profile
import com.example.socialnetworking.data.repository.Repository
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val paginatedProfiles: MutableLiveData<PaginatedData<Profile>> = MutableLiveData(PaginatedData())

    var searchQuery: String = String()
    val paginatedProfilesLiveData: LiveData<PaginatedData<Profile>> = paginatedProfiles

    fun getProfiles() {
        if (paginatedProfiles.value?.networkStatus == NetworkStatus.LOADING) return

        paginatedProfiles.value = PaginatedData(networkStatus = NetworkStatus.LOADING)

        fetchProfiles()
    }

    fun getNextPage() {
        if (!paginatedProfiles.value!!.shouldFetch()) return

        paginatedProfiles.value = paginatedProfiles.value?.stateLoading()

        fetchProfiles()
    }

    private fun fetchProfiles() {
        viewModelScope.launch {
            try {
                val profiles = Repository.search(
                    query = searchQuery,
                    start = paginatedProfiles.value!!.data.size
                )
                paginatedProfiles.value = paginatedProfiles.value?.stateSuccess(profiles)
            } catch (exception: Exception) {
                paginatedProfiles.value = paginatedProfiles.value?.stateError(exception)
            }
        }
    }
}