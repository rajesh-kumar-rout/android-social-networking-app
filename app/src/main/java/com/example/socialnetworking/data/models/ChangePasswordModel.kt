package com.example.socialnetworking.data.models

data class ChangePasswordModel (
    var oldPassword: String = String(),
    var newPassword: String = String(),
    var confirmNewPassword: String = String()
)