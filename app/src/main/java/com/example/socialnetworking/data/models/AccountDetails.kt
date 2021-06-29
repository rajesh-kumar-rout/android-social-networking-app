package com.example.socialnetworking.data.models

import java.io.Serializable

data class AccountDetails(
    val id: Int,
    var profileImageUrl: String,
    var email: String,
    var name: String,
    var bio: String,
    var totalPosts: Int,
    var totalFollowers: Int,
    var totalFollowings: Int
): Serializable
