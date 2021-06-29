package com.example.socialnetworking.data.networking

import com.example.socialnetworking.data.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiClient {

    @GET("post/home")
    suspend fun getPosts(
        @Query("start") start: Int,
        @Query("limit") limit: Int
    ): Response<MutableList<PostResponse>>

    @GET("post")
    suspend fun getOwnPosts(
        @Query("start") start: Int,
        @Query("limit") limit: Int
    ): Response<MutableList<PostResponse>>

    @Multipart
    @POST("post/create")
    suspend fun createPost(
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<Unit>

    @DELETE("post/{post}/destroy")
    suspend fun deletePost(
        @Path("post") postId: Int
    ): Response<Unit>

    @POST("post/{post}/like")
    suspend fun like(
        @Path("post") postId: Int
    ): Response<Unit>

    @DELETE("post/{post}/remove-like")
    suspend fun removeLike(
        @Path("post") postId: Int
    ): Response<Unit>

    @GET("comment/{post}")
    suspend fun getComments(
        @Path("post") postId: Int,
        @Query("start") start: Int,
        @Query("limit") limit: Int
    ): Response<MutableList<CommentResponse>>

    @FormUrlEncoded
    @POST("comment/{post}/create")
    suspend fun createComment(
        @Path("post") postId: Int,
        @Field("comment") comment: String
    ): Response<Comment>

    @DELETE("comment/{comment}/destroy")
    suspend fun deleteComment(
        @Path("comment") commentId: Int
    ): Response<Unit>

    @GET("account/followers")
    suspend fun getFollowers(
        @Query("start") start: Int,
        @Query("limit") limit: Int
    ): Response<MutableList<Profile>>

    @GET("account/followings")
    suspend fun getFollowings(
        @Query("start") start: Int,
        @Query("limit") limit: Int
    ): Response<MutableList<Profile>>

    @GET("account/details")
    suspend fun getAccountDetails(): Response<AccountDetails>

    @Multipart
    @POST("account/edit")
    suspend fun editAccount(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("bio") bio: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<String>

    @POST("account/edit")
    suspend fun editAccount(
        @Body editAccountModel: EditAccountModel
    ): Response<Unit>

    @GET("search/{query}")
    suspend fun search(
        @Path("query") query: String,
        @Query("start") start: Int,
        @Query("limit") limit: Int
    ): Response<MutableList<Profile>>

    @GET("profile/{user}/details")
    suspend fun getProfileDetails(
        @Path("user") userId: Int
    ): Response<ProfileDetailsResponse>

    @POST("profile/{user}/follow")
    suspend fun follow(
        @Path("user") userId: Int
    ): Response<Unit>

    @DELETE("profile/{user}/un-follow")
    suspend fun unFollow(
        @Path("user") userId: Int
    ): Response<Unit>

    @DELETE("auth/logout")
    suspend fun logout(): Response<Void>

    @POST("auth/change-password")
    suspend fun changePassword(
        @Body changePasswordModel: ChangePasswordModel
    ): Response<String>

    @POST("auth/login")
    suspend fun login(
        @Body loginModel: LoginModel
    ): Response<String>

    @POST("auth/sign-up")
    suspend fun signUp(
        @Body signUpModel: SignUpModel
    ): Response<Unit>
}