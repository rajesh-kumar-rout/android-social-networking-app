package com.example.socialnetworking.data.models

data class SignUpModel(
    var name: String? = null,
    var bio: String? = null,
    var email: String? = null,
    var password: String? = null,
    var confirmPassword: String? = null
)