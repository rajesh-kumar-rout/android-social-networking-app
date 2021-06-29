package com.example.socialnetworking.utils.mapper

import com.example.socialnetworking.data.models.Comment
import com.example.socialnetworking.data.models.CommentResponse

object CommentMapper {

    fun from(commentResponses: MutableList<CommentResponse>): MutableList<Comment> {
        return commentResponses.map { commentResponse ->
            Comment(
                id = commentResponse.id,
                comment = commentResponse.comment,
                name = commentResponse.name,
                profileImageUrl = commentResponse.profileImageUrl,
                isMyComment = commentResponse.isMyComment == 1
            )
        }.toMutableList()
    }
}