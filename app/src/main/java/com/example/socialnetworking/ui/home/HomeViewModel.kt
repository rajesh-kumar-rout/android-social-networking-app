package com.example.socialnetworking.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialnetworking.data.models.PaginatedData
import com.example.socialnetworking.data.models.Post
import com.example.socialnetworking.data.repository.Repository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val paginatedPosts: MutableLiveData<PaginatedData<Post>> = MutableLiveData(PaginatedData())
    val paginatedPostsLiveData: LiveData<PaginatedData<Post>> = paginatedPosts

    init {
        fetchPosts()
    }

    fun fetchPosts() {
        if (!paginatedPosts.value!!.shouldFetch()) return

        viewModelScope.launch {
            paginatedPosts.value = paginatedPosts.value?.stateLoading()
            try {
                val posts = Repository.getPosts(paginatedPosts.value!!.data.size)
                paginatedPosts.value = paginatedPosts.value?.stateSuccess(posts)
            } catch (exception: Exception) {
                paginatedPosts.value = paginatedPosts.value?.stateError(exception)
            }
        }
    }

    fun likePost(post: Post) {
        if (post.isLiked) {
            post.isLiked = false
            post.totalLikes--
            paginatedPosts.value = paginatedPosts.value
            viewModelScope.launch { Repository.removeLike(post.id) }
        } else {
            post.isLiked = true
            post.totalLikes++
            paginatedPosts.value = paginatedPosts.value
            viewModelScope.launch { Repository.like(post.id) }
        }
    }
}
