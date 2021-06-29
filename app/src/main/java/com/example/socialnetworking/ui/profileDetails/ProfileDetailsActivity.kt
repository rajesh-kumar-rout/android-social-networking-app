package com.example.socialnetworking.ui.profileDetails

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.socialnetworking.R
import com.example.socialnetworking.databinding.LayoutProfileDetailsBinding
import com.example.socialnetworking.utils.EXTRA_PROFILE_ID
import com.example.socialnetworking.utils.extensions.createProgressDialog
import com.example.socialnetworking.utils.extensions.load
import com.example.socialnetworking.utils.extensions.setTitleAndHomeButton
import com.example.socialnetworking.utils.extensions.showSnackBar

class ProfileDetailsActivity : AppCompatActivity() {

    private lateinit var binding: LayoutProfileDetailsBinding
    private lateinit var viewModel: ProfileDetailsViewModel
    private lateinit var progressDialog: AlertDialog

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutProfileDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        subscribeObserver()
        setClickListenerToViews()
    }

    private fun init() {
        setTitleAndHomeButton(getString(R.string.details))
        val profileId = intent.getIntExtra(EXTRA_PROFILE_ID, -1)
        viewModel = ViewModelProvider(this, ProfileDetailsViewModelFactory(profileId)).get(ProfileDetailsViewModel::class.java)
        progressDialog = createProgressDialog()
    }

    private fun subscribeObserver() {
        viewModel.profileDetailsLiveData.observe(this, Observer {  profileDetails ->
            binding.layoutProgressbar.root.visibility = View.GONE
            binding.profileImage.load(profileDetails.profileImageUrl, R.drawable.ic_account_circle)
            binding.name.text = profileDetails.name
            binding.description.text = profileDetails.bio
            binding.totalPost.text = profileDetails.totalPosts.toString()
            binding.totalFollower.text = profileDetails.totalFollowers.toString()
            binding.totalFollowing.text = profileDetails.totalFollowings.toString()
            binding.btnFooter.text =
                if (profileDetails.isFollowing) getString(R.string.un_follow)
                else getString(R.string.follow)
        })

        viewModel.stateDetailsLoadingLiveData.observe(this, Observer {  isLoading ->
            if(isLoading){
                binding.layoutProgressbar.root.visibility = View.VISIBLE
                binding.layoutError.root.visibility = View.GONE
            }else{
                binding.layoutProgressbar.root.visibility = View.GONE
            }
        })

        viewModel.stateDetailsErrorLiveData.observe(this, Observer { errorMessage ->
            binding.layoutError.root.visibility = View.VISIBLE
            binding.layoutError.tvError.text = errorMessage
        })

        viewModel.stateLoadingLiveData.observe(this, Observer { stateLoading ->
            if (stateLoading) progressDialog.show()
            else progressDialog.dismiss()
        })

        viewModel.stateErrorLiveData.observe(this, Observer { error ->
            error.data?.let { binding.root.showSnackBar(it) }
        })
    }

    private fun setClickListenerToViews() {
        binding.layoutError.btnRetry.setOnClickListener { viewModel.fetchProfileDetails() }
        binding.btnFooter.setOnClickListener { viewModel.follow() }
    }
}