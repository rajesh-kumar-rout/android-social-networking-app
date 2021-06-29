package com.example.socialnetworking.data.repository

import com.example.socialnetworking.data.models.ChangePasswordModel
import com.example.socialnetworking.data.models.LoginModel
import com.example.socialnetworking.data.models.SignUpModel
import com.example.socialnetworking.data.networking.ApiClient
import com.example.socialnetworking.data.networking.ApiService
import com.example.socialnetworking.data.preference.PreferenceManager

object AuthRepository {

    private val remoteDataSource: ApiClient = ApiService.remoteDataSource

    suspend fun signUp(signUpModel: SignUpModel) {
        return ApiService.fetch { remoteDataSource.signUp(signUpModel) }
    }

    fun isLoggedIn(): Boolean {
        return PreferenceManager.hasToken()
    }

    suspend fun logout() {
        try {
            ApiService.fetch { remoteDataSource.logout() }
        } catch (exception: Exception) {}finally {
            PreferenceManager.deleteToken()
        }
    }

    suspend fun changePassword(changePasswordModel: ChangePasswordModel){
        val token = ApiService.fetch { remoteDataSource.changePassword(changePasswordModel) }
        PreferenceManager.setToken(token)
    }

    suspend fun login(loginModel: LoginModel) {
        val token = ApiService.fetch { remoteDataSource.login(loginModel) }
        PreferenceManager.setToken(token)
    }
}