package com.example.socialnetworking.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.socialnetworking.data.models.*
import com.example.socialnetworking.data.networking.ApiClient
import com.example.socialnetworking.data.networking.ApiService
import com.example.socialnetworking.utils.*
import com.example.socialnetworking.utils.mapper.CommentMapper
import com.example.socialnetworking.utils.mapper.PostMapper
import com.example.socialnetworking.utils.mapper.ProfileDetailsMapper
import kotlinx.coroutines.flow.Flow
import okhttp3.*
import java.io.File

object Repository {

    private var remoteDataSource: ApiClient = ApiService.remoteDataSource
    private val accountDetails = MutableLiveData<AccountDetails>()
    val accountDetailsLiveData: LiveData<AccountDetails> = accountDetails

    suspend fun getPosts(start: Int): MutableList<Post> {
        return PostMapper.fromList(ApiService.fetch { remoteDataSource.getPosts(start, PAGINATION_PER_PAGE_LIMIT) })
    }

    suspend fun getOwnPosts(start: Int): MutableList<Post> {
        return PostMapper.fromList(ApiService.fetch { remoteDataSource.getOwnPosts(start, PAGINATION_PER_PAGE_LIMIT) })
    }

    suspend fun deletePost(postId: Int) {
        ApiService.fetch { remoteDataSource.deletePost(postId) }
        accountDetails.value?.let { it.totalPosts-- }
        accountDetails.value = accountDetails.value
    }

    suspend fun createPost(file: File, description: String) {
        val fileBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val descriptionBody: RequestBody = RequestBody.create(MediaType.parse("text/plain"), description)
        val filePart: MultipartBody.Part = MultipartBody.Part.createFormData("image", file.name, fileBody)
        ApiService.fetch { remoteDataSource.createPost(descriptionBody, filePart) }
        accountDetails.value?.let { it.totalPosts++ }
        accountDetails.value = accountDetails.value
    }

    suspend fun like(postId: Int) {
        try { remoteDataSource.like(postId) } catch (exception: Exception) { }
    }

    suspend fun removeLike(postId: Int) {
        try { remoteDataSource.removeLike(postId) } catch (exception: Exception) { }
    }

    suspend fun getComments(postId: Int, start: Int): MutableList<Comment> {
        val commentResponses = ApiService.fetch { remoteDataSource.getComments(postId, start, PAGINATION_PER_PAGE_LIMIT) }
        return CommentMapper.from(commentResponses)
    }

    suspend fun createComment(postId: Int, comment: String): Comment {
        return ApiService.fetch { remoteDataSource.createComment(postId, comment) }
    }

    suspend fun deleteComment(commentId: Int) {
        return ApiService.fetch { remoteDataSource.deleteComment(commentId) }
    }

    suspend fun getFollowers(start: Int): MutableList<Profile> {
        return ApiService.fetch { remoteDataSource.getFollowers(start, PAGINATION_PER_PAGE_LIMIT) }
    }

    suspend fun getFollowings(start: Int): MutableList<Profile> {
        return ApiService.fetch { remoteDataSource.getFollowings(start, PAGINATION_PER_PAGE_LIMIT) }
    }

    suspend fun getAccountDetails() {
        accountDetails.value = ApiService.fetch { remoteDataSource.getAccountDetails() }
    }

    suspend fun search(query: String, start: Int): MutableList<Profile> {
        return ApiService.fetch { remoteDataSource.search(query, start, PAGINATION_PER_PAGE_LIMIT) }
    }

    suspend fun getProfileDetails(profileId: Int): ProfileDetails {
        return ProfileDetailsMapper.from(ApiService.fetch { remoteDataSource.getProfileDetails(profileId) })
    }

    suspend fun editAccount(editAccountModel: EditAccountModel, profileImage: File? = null){
        if(profileImage == null){
            ApiService.fetch { remoteDataSource.editAccount(editAccountModel) }
        }else{
            val imageBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), profileImage)
            val nameRequest: RequestBody = RequestBody.create(MediaType.parse("text/plain"), editAccountModel.name!!)
            val emailRequest: RequestBody = RequestBody.create(MediaType.parse("text/plain"), editAccountModel.email!!)
            val bioRequest: RequestBody = RequestBody.create(MediaType.parse("text/plain"), editAccountModel.bio!!)
            val profileImageRequest: MultipartBody.Part = MultipartBody.Part.createFormData("image", profileImage.name, imageBody)
            val profileImageUrl = ApiService.fetch { remoteDataSource.editAccount(nameRequest, emailRequest, bioRequest, profileImageRequest) }
            accountDetails.value?.profileImageUrl = profileImageUrl
        }
        accountDetails.value?.name = editAccountModel.name!!
        accountDetails.value?.email = editAccountModel.email!!
        accountDetails.value?.bio = editAccountModel.bio!!
        accountDetails.value = accountDetails.value
    }

    suspend fun follow(userId: Int) {
        ApiService.fetch { remoteDataSource.follow(userId) }
        accountDetails.value?.let { it.totalFollowings++ }
        accountDetails.value = accountDetails.value
    }

    suspend fun unFollow(userId: Int) {
        ApiService.fetch { remoteDataSource.unFollow(userId) }
        accountDetails.value?.let { it.totalFollowings-- }
        accountDetails.value = accountDetails.value
    }
}