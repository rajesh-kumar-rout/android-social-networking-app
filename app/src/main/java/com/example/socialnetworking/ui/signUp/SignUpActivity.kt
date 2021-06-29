package com.example.socialnetworking.ui.signUp

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.socialnetworking.R
import com.example.socialnetworking.databinding.ActivitySignUpBinding
import com.example.socialnetworking.utils.extensions.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignUpViewModel
    private lateinit var progressDialog: AlertDialog

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        subscribeObservers()
        setListenerToViews()
    }

    private fun setListenerToViews() {
        binding.btnSignUp.setOnClickListener {
            viewModel.signUp()
        }

        binding.btnLogin.setOnClickListener {
            onBackPressed()
        }

        binding.tiName.onTextChange(viewModel::changeName)
        binding.tiEmail.onTextChange(viewModel::changeEmail)
        binding.tiBio.onTextChange(viewModel::changeBio)
        binding.tiPassword.onTextChange(viewModel::changePassword)
        binding.tiConfirmPassword.onTextChange(viewModel::changeConfirmPassword)
    }

    private fun init() {
        setTitleAndHomeButton(getString(R.string.sign_up))
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        progressDialog = createProgressDialog()
    }

    private fun subscribeObservers() {
        viewModel.nameErrorLiveData.observe(this, Observer { errorMessage ->
            binding.tiName.setErrorMessage(errorMessage)
        })

        viewModel.bioErrorLiveData.observe(this, Observer { errorMessage ->
            binding.tiBio.setErrorMessage(errorMessage)
        })

        viewModel.emailErrorLiveData.observe(this, Observer { errorMessage ->
            binding.tiEmail.setErrorMessage(errorMessage)
        })

        viewModel.passwordErrorLiveData.observe(this, Observer { errorMessage ->
            binding.tiPassword.setErrorMessage(errorMessage)
        })

        viewModel.confirmPasswordErrorLiveData.observe(this, Observer { errorMessage ->
            binding.tiConfirmPassword.setErrorMessage(errorMessage)
        })

        viewModel.stateLoadingLiveData.observe(this, Observer { stateLoading ->
            if (stateLoading) progressDialog.show()
            else progressDialog.dismiss()
        })

        viewModel.stateSuccessLiveData.observe(this, Observer {
            showToast(getString(R.string.msg_signed_up))
            onBackPressed()
        })

        viewModel.stateErrorLiveData.observe(this, Observer { stateError ->
            stateError.data?.let { binding.root.showSnackBar(it) }
        })
    }
}