package com.example.socialnetworking.ui.profileDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProfileDetailsViewModelFactory(
    private val profileId: Int
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Int::class.java).newInstance(profileId)
    }
}