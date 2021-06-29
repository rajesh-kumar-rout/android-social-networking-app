package com.example.socialnetworking.data.models

data class CommentResponse(
    val id: Int,
    val name: String,
    val profileImageUrl: String,
    val comment: String,
    val isMyComment: Int
)