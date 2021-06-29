package com.example.socialnetworking.data.models

data class Post (
    val id: Int,
    val profileImageUrl: String,
    val name: String,
    val createdAt: String,
    val postImageUrl: String,
    val description: String,
    var totalLikes: Int,
    var isLiked: Boolean
)