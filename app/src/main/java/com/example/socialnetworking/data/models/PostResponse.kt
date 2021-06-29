package com.example.socialnetworking.data.models

data class PostResponse(
    val id: Int,
    val profileImageUrl: String,
    val name: String,
    val createdAt: String,
    val postImageUrl: String,
    val description: String,
    var totalLikes: Int,
    var isLiked: Int
)
