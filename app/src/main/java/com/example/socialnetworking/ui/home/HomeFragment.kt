package com.example.socialnetworking.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialnetworking.R
import com.example.socialnetworking.ui.comment.CommentActivity
import com.example.socialnetworking.databinding.FragmentHomeBinding
import com.example.socialnetworking.databinding.LayoutRecyclerviewBinding
import com.example.socialnetworking.utils.*
import com.example.socialnetworking.utils.adapters.PostAdapter
import com.example.socialnetworking.utils.extensions.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var viewModel: HomeViewModel
    private lateinit var recyclerviewBinding: LayoutRecyclerviewBinding
    private lateinit var adapter: PostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        subscribeObservers()
    }

    private fun init(view: View) {
        val homeBinding: FragmentHomeBinding = FragmentHomeBinding.bind(view)
        recyclerviewBinding = homeBinding.layoutRecyclerview
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        adapter = PostAdapter(viewModel::fetchPosts, this::onCommentBtnClick, requireContext(), null, viewModel::likePost)
        homeBinding.appbar.toolbar.title = getString(R.string.home)
        recyclerviewBinding.init(requireContext(), adapter, viewModel::fetchPosts)
    }

    private fun onCommentBtnClick(postId: Int) {
        val intent = Intent(requireActivity(), CommentActivity::class.java)
        intent.putExtra(EXTRA_POST_ID, postId)
        startActivity(intent)
    }

    private fun subscribeObservers() {
        viewModel.paginatedPostsLiveData.observe(viewLifecycleOwner, Observer { paginatedPosts ->
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
    }
}