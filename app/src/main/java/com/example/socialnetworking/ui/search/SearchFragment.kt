package com.example.socialnetworking.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.socialnetworking.R
import com.example.socialnetworking.utils.adapters.ProfileAdapter
import com.example.socialnetworking.databinding.FragmentSearchBinding
import com.example.socialnetworking.databinding.LayoutRecyclerviewBinding
import com.example.socialnetworking.ui.profileDetails.ProfileDetailsActivity
import com.example.socialnetworking.utils.*
import com.example.socialnetworking.utils.extensions.*

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var searchView: SearchView
    private lateinit var recyclerViewBinding: LayoutRecyclerviewBinding
    private lateinit var adapter: ProfileAdapter
    private lateinit var viewModel: SearchViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        setListenerToViews()
        subscribeObservers()
    }

    private fun init(view: View) {
        val fragmentSearchBinding: FragmentSearchBinding = FragmentSearchBinding.bind(view)
        searchView = fragmentSearchBinding.searchView
        recyclerViewBinding = fragmentSearchBinding.layoutRecyclerview
        viewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
        adapter = ProfileAdapter(viewModel::getNextPage, this::onProfileClick)
        recyclerViewBinding.init(requireContext(), adapter, viewModel::getProfiles)
    }

    private fun onProfileClick(profileId: Int) {
        val intent = Intent(requireActivity(), ProfileDetailsActivity::class.java)
        intent.putExtra(EXTRA_PROFILE_ID, profileId)
        startActivity(intent)
    }

    private fun setListenerToViews() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchQuery = newText!!
                return false
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getProfiles()
                return false
            }
        })
    }

    private fun subscribeObservers() {
        viewModel.paginatedProfilesLiveData.observe(viewLifecycleOwner, Observer { paginatedProfiles ->
            when {
                paginatedProfiles.isEmpty() -> {
                    recyclerViewBinding.empty(getString(R.string.msg_no_profiles))
                }

                paginatedProfiles.isInitialLoading() -> {
                    recyclerViewBinding.loading()
                }

                paginatedProfiles.isInitialError() -> {
                    recyclerViewBinding.error(paginatedProfiles.errorMessage)
                }

                else -> {
                    recyclerViewBinding.hasData()
                    recyclerViewBinding.recyclerview.post { adapter.setProfilesAndNotifyAdapter(paginatedProfiles) }
                }
            }
        })
    }
}