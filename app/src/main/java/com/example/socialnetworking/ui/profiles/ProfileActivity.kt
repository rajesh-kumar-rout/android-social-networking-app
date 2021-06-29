package com.example.socialnetworking.ui.profiles

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.socialnetworking.R
import com.example.socialnetworking.utils.adapters.ProfileAdapter
import com.example.socialnetworking.databinding.LayoutRecyclerviewBinding
import com.example.socialnetworking.ui.profileDetails.ProfileDetailsActivity
import com.example.socialnetworking.utils.*
import com.example.socialnetworking.utils.extensions.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var recyclerViewBinding: LayoutRecyclerviewBinding
    private lateinit var viewModel: ProfilesViewModel
    private lateinit var adapter: ProfileAdapter

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recyclerViewBinding = LayoutRecyclerviewBinding.inflate(layoutInflater)
        setContentView(recyclerViewBinding.root)

        init()
        subscribeObservers()
    }

    private fun init() {
        setTitleAndHomeButton(intent.getStringExtra(EXTRA_TITLE)!!)
        val action = intent.getStringExtra(EXTRA_ACTION)
        viewModel = ViewModelProvider(this, ProfileViewModelFactory(action)).get(ProfilesViewModel::class.java)
        adapter = ProfileAdapter(viewModel::fetchProfiles, this::onProfileClick)
        recyclerViewBinding.init(this, adapter, viewModel::fetchProfiles)
    }

    private fun onProfileClick(profileId: Int) {
        val intent = Intent(this, ProfileDetailsActivity::class.java)
        intent.putExtra(EXTRA_PROFILE_ID, profileId)
        startActivity(intent)
    }

    private fun subscribeObservers() {
        viewModel.paginatedProfiles.observe(this, Observer { paginatedProfiles ->
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