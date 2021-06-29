package com.example.socialnetworking.data.models

data class Comment (
    val id: Int,
    val name: String,
    val profileImageUrl: String,
    val comment: String,
    val isMyComment: Boolean
)