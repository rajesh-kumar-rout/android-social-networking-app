package com.example.socialnetworking.utils.mapper

import com.example.socialnetworking.data.models.ProfileDetails
import com.example.socialnetworking.data.models.ProfileDetailsResponse

object ProfileDetailsMapper{

    fun from(profileDetailsResponse: ProfileDetailsResponse): ProfileDetails{
        return ProfileDetails(
            id = profileDetailsResponse.id,
            profileImageUrl = profileDetailsResponse.profileImageUrl,
            name = profileDetailsResponse.name,
            bio = profileDetailsResponse.bio,
            totalPosts = profileDetailsResponse.totalPosts,
            totalFollowers = profileDetailsResponse.totalFollowers,
            totalFollowings = profileDetailsResponse.totalFollowings,
            isFollowing = profileDetailsResponse.isFollowing == 1
        )
    }
}