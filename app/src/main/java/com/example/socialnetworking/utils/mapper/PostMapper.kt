package com.example.socialnetworking.utils.mapper

import com.example.socialnetworking.data.models.Post
import com.example.socialnetworking.data.models.PostResponse
import com.example.socialnetworking.utils.DateConverter

object PostMapper {

    fun fromList(postResponses: MutableList<PostResponse>): MutableList<Post> {
        return postResponses.map { postResponse ->
            Post(
                id = postResponse.id,
                profileImageUrl = postResponse.profileImageUrl,
                name = postResponse.name,
                createdAt =  DateConverter.convertStringToRelativeTime(postResponse.createdAt),
                postImageUrl = postResponse.postImageUrl,
                description = postResponse.description,
                totalLikes = postResponse.totalLikes,
                isLiked = postResponse.isLiked == 1
            )
        }.toMutableList()
    }
}