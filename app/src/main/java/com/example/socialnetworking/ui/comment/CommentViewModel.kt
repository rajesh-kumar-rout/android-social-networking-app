package com.example.socialnetworking.ui.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialnetworking.data.models.Comment
import com.example.socialnetworking.data.models.PaginatedData
import com.example.socialnetworking.data.repository.Repository
import com.example.socialnetworking.utils.Event
import kotlinx.coroutines.launch

class CommentViewModel(
    private val postId: Int
) : ViewModel() {

    private val paginatedComments: MutableLiveData<PaginatedData<Comment>> = MutableLiveData(PaginatedData())
    private val stateLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val stateError: MutableLiveData<Event<String>> = MutableLiveData()

    val paginatedCommentsLiveData: LiveData<PaginatedData<Comment>> = paginatedComments
    val stateLoadingLiveData: LiveData<Boolean> = stateLoading
    val stateErrorLiveData: LiveData<Event<String>> = stateError

    init {
        fetchComments()
    }

    fun deleteComment(comment: Comment) {
        viewModelScope.launch {
            stateLoading.value = true
            try {
                Repository.deleteComment(comment.id)
                paginatedComments.value = paginatedComments.value?.remove(comment)
            } catch (exception: Exception) {
                stateError.value = Event(exception.message)
            }
            stateLoading.value = false
        }
    }

    fun createComment(comment: String) {
        if (comment.isEmpty()) return

        viewModelScope.launch {
            stateLoading.value = true
            try {
                val newComment = Repository.createComment(postId, comment)
                paginatedComments.value = paginatedComments.value?.addAt(0, newComment)
            } catch (exception: Exception) {
                stateError.value = Event(exception.message)
            }
            stateLoading.value = false
        }
    }

    fun fetchComments() {
        if (!paginatedComments.value!!.shouldFetch()) return

        viewModelScope.launch {
            paginatedComments.value = paginatedComments.value?.stateLoading()
            try {
                val comments = Repository.getComments(postId, paginatedComments.value!!.data.size)
                paginatedComments.value = paginatedComments.value?.stateSuccess(comments)
            } catch (exception: Exception) {
                paginatedComments.value = paginatedComments.value?.stateError(exception)
            }
        }
    }
}
