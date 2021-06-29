package com.example.socialnetworking.ui.accountDetails

import androidx.lifecycle.*
import com.example.socialnetworking.data.models.AccountDetails
import com.example.socialnetworking.data.repository.AuthRepository
import com.example.socialnetworking.data.repository.Repository
import com.example.socialnetworking.utils.Event
import kotlinx.coroutines.launch

class AccountDetailsViewModel() : ViewModel() {

    private val navigateToEditAccount: MutableLiveData<Event<AccountDetails>> = MutableLiveData()
    private val stateLogoutLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val stateLogoutSuccess: MutableLiveData<Boolean> = MutableLiveData()
    private val accountDetails: MutableLiveData<AccountDetails> = MutableLiveData()
    private val stateDetailsLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val stateDetailsError: MutableLiveData<String> = MutableLiveData()

    val navigateToEditAccountLiveData: LiveData<Event<AccountDetails>> = navigateToEditAccount
    val stateLogoutLoadingLiveData: LiveData<Boolean> = stateLogoutLoading
    val stateLogoutSuccessLiveData: LiveData<Boolean> = stateLogoutSuccess
    val accountDetailsLiveData: LiveData<AccountDetails> = accountDetails
    val stateDetailsLoadingLiveData: LiveData<Boolean> = stateDetailsLoading
    val stateDetailsErrorLiveData: LiveData<String> = stateDetailsError

    init {
        fetchAccountDetails()
        observeAccountDetails()
    }

    fun navigateToEditAccount() {
        navigateToEditAccount.value = Event(accountDetails.value!!)
    }

    fun logout() {
        viewModelScope.launch {
            stateLogoutLoading.value = true
            AuthRepository.logout()
            stateLogoutSuccess.value = true
        }
    }

    fun fetchAccountDetails() {
        viewModelScope.launch {
            stateDetailsLoading.value = true
            try {
                Repository.getAccountDetails()
            } catch (exception: Exception) {
                stateDetailsError.value = exception.message
            }
            stateDetailsLoading.value = false
        }
    }

    private fun observeAccountDetails(){
        Repository.accountDetailsLiveData.observeForever { details -> accountDetails.value = details }
    }
}