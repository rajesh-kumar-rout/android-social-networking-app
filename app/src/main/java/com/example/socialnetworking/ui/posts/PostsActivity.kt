package com.example.socialnetworking.ui.posts

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.socialnetworking.R
import com.example.socialnetworking.databinding.LayoutRecyclerviewBinding
import com.example.socialnetworking.ui.comment.CommentActivity
import com.example.socialnetworking.utils.EXTRA_POST_ID
import com.example.socialnetworking.utils.adapters.PostAdapter
import com.example.socialnetworking.utils.extensions.*

class PostsActivity: AppCompatActivity() {

    private lateinit var recyclerviewBinding: LayoutRecyclerviewBinding
    private lateinit var viewModel: PostsActivityViewModel
    private lateinit var adapter: PostAdapter
    private lateinit var progressDialog: AlertDialog

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recyclerviewBinding = LayoutRecyclerviewBinding.inflate(layoutInflater)
        setContentView(recyclerviewBinding.root)

        init()
        subscribeObservers()
    }

    private fun init(){
        setTitleAndHomeButton(getString(R.string.posts))
        viewModel = ViewModelProvider(this).get(PostsActivityViewModel::class.java)
        progressDialog = createProgressDialog()
        adapter = PostAdapter(viewModel::fetchPosts, this::onCommentBtnClick, this, viewModel::deletePost)
        recyclerviewBinding.init(this, adapter, viewModel::fetchPosts)
    }

    private fun onCommentBtnClick(postId: Int){
        val intent = Intent(this, CommentActivity::class.java)
        intent.putExtra(EXTRA_POST_ID, postId)
        startActivity(intent)
    }

    private fun subscribeObservers(){
        viewModel.paginatedPostsLiveData.observe(this, Observer { paginatedPosts ->
            when {
                paginatedPosts.isEmpty() -> {
                    recyclerviewBinding.empty(getString(R.string.msg_no_post))
                }

                paginatedPosts.isInitialLoading() -> {
                    recyclerviewBinding.loading()
                }

                paginatedPosts.isInitialError() -> {
                    recyclerviewBinding.error(paginatedPosts.errorMessage)
                }

                else -> {
                    recyclerviewBinding.hasData()
                    recyclerviewBinding.recyclerview.post { adapter.setPostsAndNotifyAdapter(paginatedPosts) }
                }
            }
        })

        viewModel.stateLoadingLiveData.observe(this, Observer { stateLoading ->
            if(stateLoading) progressDialog.show()
            else progressDialog.dismiss()
        })

        viewModel.stateErrorLiveData.observe(this, Observer { stateError ->
            stateError.data?.let { recyclerviewBinding.root.showSnackBar(it) }
        })
    }
}