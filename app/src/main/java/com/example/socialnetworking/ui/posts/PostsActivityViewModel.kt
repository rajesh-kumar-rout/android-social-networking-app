package com.example.socialnetworking.ui.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialnetworking.data.models.PaginatedData
import com.example.socialnetworking.data.models.Post
import com.example.socialnetworking.data.repository.Repository
import com.example.socialnetworking.utils.Event
import kotlinx.coroutines.launch

class PostsActivityViewModel : ViewModel() {

    private val paginatedPosts: MutableLiveData<PaginatedData<Post>> = MutableLiveData(PaginatedData())
    private val stateLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val stateError: MutableLiveData<Event<String>> = MutableLiveData()

    val paginatedPostsLiveData: LiveData<PaginatedData<Post>> = paginatedPosts
    val stateLoadingLiveData: LiveData<Boolean> = stateLoading
    val stateErrorLiveData: LiveData<Event<String>> = stateError

    init {
        fetchPosts()
    }

    fun fetchPosts() {
        if (!paginatedPosts.value!!.shouldFetch()) return

        viewModelScope.launch {
            paginatedPosts.value = paginatedPosts.value?.stateLoading()
            try {
                paginatedPosts.value = paginatedPosts.value!!.stateSuccess(
                    Repository.getOwnPosts(paginatedPosts.value!!.data.size)
                )
            } catch (exception: Exception) {
                paginatedPosts.value = paginatedPosts.value!!.stateError(exception)
            }
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch {
            stateLoading.value = true
            try {
                Repository.deletePost(post.id)
                paginatedPosts.value = paginatedPosts.value?.remove(post)
            } catch (exception: Exception) {
                stateError.value = Event(exception.message)
            }
            stateLoading.value = false
        }
    }
}