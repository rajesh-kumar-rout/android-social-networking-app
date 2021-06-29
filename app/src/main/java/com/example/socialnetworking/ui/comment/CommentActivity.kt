package com.example.socialnetworking.ui.comment

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.socialnetworking.R
import com.example.socialnetworking.databinding.*
import com.example.socialnetworking.utils.*
import com.example.socialnetworking.utils.extensions.*

class CommentActivity : AppCompatActivity() {

    private lateinit var viewModel: CommentViewModel
    private lateinit var adapter: CommentAdapter
    private lateinit var progressDialog: AlertDialog
    private lateinit var recyclerViewBinding: LayoutRecyclerviewBinding
    private lateinit var commentBinding: ActivityCommentBinding

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        commentBinding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(commentBinding.root)

        init()
        setClickListenerToViews()
        subscribeObservers()
    }

    private fun init() {
        recyclerViewBinding = commentBinding.recyclerviewLayout
        viewModel = ViewModelProvider(this, CommentViewModelFactory(intent.getIntExtra(EXTRA_POST_ID, -1))).get(CommentViewModel::class.java)
        adapter = CommentAdapter(viewModel::fetchComments, viewModel::deleteComment)
        progressDialog = createProgressDialog()
        recyclerViewBinding.init(this, adapter, viewModel::fetchComments)
        setTitleAndHomeButton(getString(R.string.comments))
    }

    private fun subscribeObservers() {
        viewModel.paginatedCommentsLiveData.observe(this, Observer { paginatedComments ->
            when {
                paginatedComments.isEmpty() -> {
                    recyclerViewBinding.empty(getString(R.string.msg_no_comment))
                }

                paginatedComments.isInitialLoading() -> {
                    recyclerViewBinding.loading()
                }

                paginatedComments.isInitialError() -> {
                    recyclerViewBinding.error(paginatedComments.errorMessage)
                }

                else -> {
                    recyclerViewBinding.hasData()
                    recyclerViewBinding.recyclerview.post { adapter.setCommentsAndNotifyAdapter(paginatedComments) }
                }
            }
        })

        viewModel.stateLoadingLiveData.observe(this, Observer { stateLoading ->
            if (stateLoading) progressDialog.show()
            else progressDialog.dismiss()
        })

        viewModel.stateErrorLiveData.observe(this, Observer { stateError ->
            stateError.data?.let { commentBinding.root.showSnackBar(it) }
        })
    }

    private fun setClickListenerToViews() {
        commentBinding.btnSend.setOnClickListener { viewModel.createComment(commentBinding.etComment.text.toString()) }
    }
}