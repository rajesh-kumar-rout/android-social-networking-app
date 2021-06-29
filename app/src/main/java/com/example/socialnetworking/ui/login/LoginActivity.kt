package com.example.socialnetworking.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.socialnetworking.R
import com.example.socialnetworking.databinding.ActivityLoginBinding
import com.example.socialnetworking.ui.main.MainActivity
import com.example.socialnetworking.ui.signUp.SignUpActivity
import com.example.socialnetworking.utils.extensions.createProgressDialog
import com.example.socialnetworking.utils.extensions.onTextChange
import com.example.socialnetworking.utils.extensions.setErrorMessage
import com.example.socialnetworking.utils.extensions.showSnackBar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var progressDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        subscribeObservers()
        setListenerToViews()
    }

    private fun init() {
        title = getString(R.string.login)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        progressDialog = createProgressDialog()
    }

    private fun setListenerToViews() {
        binding.tiEmail.onTextChange(viewModel::changeEmail)
        binding.tiPassword.onTextChange(viewModel::changePassword)
        binding.btnLogin.setOnClickListener { viewModel.login() }
        binding.btnSignUp.setOnClickListener { startActivity(Intent(this, SignUpActivity::class.java)) }
    }

    private fun subscribeObservers() {
        viewModel.emailErrorLiveData.observe(this, Observer { errorMessage ->
            binding.tiEmail.setErrorMessage(errorMessage)
        })

        viewModel.passwordErrorLiveData.observe(this, Observer { errorMessage ->
            binding.tiPassword.setErrorMessage(errorMessage)
        })

        viewModel.stateLoadingLiveData.observe(this, Observer { stateLoading ->
            if (stateLoading) progressDialog.show()
            else progressDialog.dismiss()
        })

        viewModel.stateSuccessLiveData.observe(this, Observer { stateSuccess ->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        })

        viewModel.stateErrorLiveData.observe(this, Observer { stateError ->
            stateError.data?.let { binding.root.showSnackBar(it) }
        })
    }
}