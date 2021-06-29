package com.example.socialnetworking.ui.changePassword

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.socialnetworking.R
import com.example.socialnetworking.databinding.ActivityChangePasswordBinding
import com.example.socialnetworking.utils.extensions.*

class ChangePasswordActivity: AppCompatActivity() {

    private lateinit var changePasswordBinding: ActivityChangePasswordBinding
    private lateinit var viewModel: ChangePasswordViewModel
    private lateinit var progressDialog: AlertDialog

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changePasswordBinding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(changePasswordBinding.root)

        init()
        setListenerToViews()
        subscribeObservers()
    }

    private fun init(){
        setTitleAndHomeButton(getString(R.string.change_password))
        viewModel = ViewModelProvider(this).get(ChangePasswordViewModel::class.java)
        progressDialog = createProgressDialog()
    }

    private fun setListenerToViews(){
        changePasswordBinding.btnChangePassword.setOnClickListener { viewModel.changePassword() }

        changePasswordBinding.tiOldPassword.onTextChange(viewModel::changeOldPassword)
        changePasswordBinding.tiNewPassword.onTextChange(viewModel::changeNewPassword)
        changePasswordBinding.tiConfirmNewPassword.onTextChange(viewModel::changeConfirmNewPassword)
    }

    private fun subscribeObservers(){
        viewModel.oldPasswordErrorLiveData.observe(this, Observer { errorMessage ->
            changePasswordBinding.tiOldPassword.setErrorMessage(errorMessage)
        })

        viewModel.newPasswordErrorLiveData.observe(this, Observer {  errorMessage ->
            changePasswordBinding.tiNewPassword.setErrorMessage(errorMessage)
        })

        viewModel.confirmNewPasswordErrorLiveData.observe(this, Observer { errorMessage ->
            changePasswordBinding.tiConfirmNewPassword.setErrorMessage(errorMessage)
        })

        viewModel.stateLoadingLiveData.observe(this, Observer {  isLoading ->
            if(isLoading) progressDialog.show()
            else progressDialog.dismiss()
        })

        viewModel.stateErrorLiveData.observe(this, Observer {  errorMessage ->
            errorMessage.data?.let { changePasswordBinding.root.showSnackBar(it) }
        })

        viewModel.stateSuccessLiveData.observe(this, Observer {
            showToast(getString(R.string.msg_password_changed))
            onBackPressed()
        })
    }
}