package com.example.socialnetworking.ui.accountDetails

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.socialnetworking.R
import com.example.socialnetworking.databinding.LayoutProfileDetailsBinding
import com.example.socialnetworking.ui.editAccount.EditAccountActivity
import com.example.socialnetworking.databinding.FragmentAccountDetailsBinding
import com.example.socialnetworking.ui.changePassword.ChangePasswordActivity
import com.example.socialnetworking.ui.login.LoginActivity
import com.example.socialnetworking.ui.posts.PostsActivity
import com.example.socialnetworking.ui.profiles.ProfileActivity
import com.example.socialnetworking.utils.*
import com.example.socialnetworking.utils.extensions.createProgressDialog
import com.example.socialnetworking.utils.extensions.load

class AccountDetailsFragment() : Fragment(R.layout.fragment_account_details) {

    private lateinit var binding: LayoutProfileDetailsBinding
    private lateinit var viewModel: AccountDetailsViewModel
    private lateinit var progressDialog: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        setClickListenerToViews()
        subscribeObservers()
    }

    private fun init(view: View) {
        val fragmentBinding = FragmentAccountDetailsBinding.bind(view)
        fragmentBinding.appbar.toolbar.title = getString(R.string.profile)
        fragmentBinding.appbar.toolbar.inflateMenu(R.menu.menu_security)
        fragmentBinding.appbar.toolbar.setOnMenuItemClickListener(this::onMenuItemClick)
        binding = fragmentBinding.layoutProfileDetails
        viewModel = ViewModelProvider(requireActivity()).get(AccountDetailsViewModel::class.java)
        binding.btnFooter.text = getString(R.string.edit_account)
        progressDialog = createProgressDialog()
    }

    private fun onMenuItemClick(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.logout) {
            viewModel.logout()
        }
        else if(menuItem.itemId == R.id.change_password){
            startActivity(Intent(requireActivity(), ChangePasswordActivity::class.java))
        }
        return true
    }

    private fun subscribeObservers() {
        viewModel.accountDetailsLiveData.observe(viewLifecycleOwner, Observer {  accountDetails ->
            binding.layoutError.root.visibility = View.GONE
            binding.profileImage.load(accountDetails.profileImageUrl, R.drawable.ic_account_circle)
            binding.name.text = accountDetails.name
            binding.description.text = accountDetails.bio
            binding.totalPost.text = accountDetails.totalPosts.toString()
            binding.totalFollower.text = accountDetails.totalFollowers.toString()
            binding.totalFollowing.text = accountDetails.totalFollowings.toString()
            binding.layoutProgressbar.root.visibility = View.GONE
        })

        viewModel.stateDetailsLoadingLiveData.observe(viewLifecycleOwner, Observer {  isLoading ->
            if(isLoading){
                binding.layoutError.root.visibility = View.GONE
                binding.layoutProgressbar.root.visibility = View.VISIBLE
            }else{
                binding.layoutProgressbar.root.visibility = View.GONE
            }
        })

        viewModel.stateDetailsErrorLiveData.observe(viewLifecycleOwner, Observer {  errorMessage ->
            binding.layoutError.root.visibility = View.VISIBLE
            binding.layoutError.tvError.text = errorMessage
            binding.layoutProgressbar.root.visibility = View.GONE
        })

        viewModel.navigateToEditAccountLiveData.observe(viewLifecycleOwner, Observer { accountDetails ->
            accountDetails.data?.let {
                val intent = Intent(requireActivity(), EditAccountActivity::class.java)
                intent.putExtra(EXTRA_ACCOUNT_DETAILS, it)
                startActivity(intent)
            }
        })

        viewModel.stateLogoutLoadingLiveData.observe(viewLifecycleOwner, Observer {stateLoading ->
            if(stateLoading) progressDialog.show()
            else progressDialog.dismiss()
        })

        viewModel.stateLogoutSuccessLiveData.observe(viewLifecycleOwner, Observer {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        })
    }

    private fun setClickListenerToViews() {
        binding.layoutError.btnRetry.setOnClickListener { viewModel.fetchAccountDetails() }

        binding.layoutFollower.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            intent.putExtra(EXTRA_ACTION, EXTRA_ACTION_FOLLOWER)
            intent.putExtra(EXTRA_TITLE, getString(R.string.follower))
            startActivity(intent)
        }

        binding.layoutFollowing.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            intent.putExtra(EXTRA_ACTION, EXTRA_ACTION_FOLLOWING)
            intent.putExtra(EXTRA_TITLE, getString(R.string.following))
            startActivity(intent)
        }

        binding.btnFooter.setOnClickListener {
            viewModel.navigateToEditAccount()
        }

        binding.layoutPosts.setOnClickListener {
            startActivity(Intent(requireActivity(), PostsActivity::class.java))
        }
    }
}