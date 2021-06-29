package com.example.socialnetworking.data.preference

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.socialnetworking.utils.App
import com.example.socialnetworking.utils.KEY_TOKEN
import com.example.socialnetworking.utils.PREFERENCE_NAME

object PreferenceManager {

    private var preferences: SharedPreferences =
        App.context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
    private val preferencesEditor: SharedPreferences.Editor = preferences.edit()

    fun setToken(token: String) {
        preferencesEditor.putString(KEY_TOKEN, token)
        preferencesEditor.apply()
    }

    fun getToken(): String? = preferences.getString(KEY_TOKEN, null)

    fun deleteToken() {
        preferencesEditor.remove(KEY_TOKEN)
        preferencesEditor.apply()
    }

    fun hasToken(): Boolean = preferences.contains(KEY_TOKEN)
}