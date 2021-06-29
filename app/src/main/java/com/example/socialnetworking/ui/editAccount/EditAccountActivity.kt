package com.example.socialnetworking.ui.editAccount

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.socialnetworking.R
import com.example.socialnetworking.databinding.ActivityEditAccountBinding
import com.example.socialnetworking.databinding.DialogLoadingBinding
import com.example.socialnetworking.utils.ALLOWED_IMAGE_MIME_TYPE
import com.example.socialnetworking.utils.ALLOWED_IMAGE_TYPE
import com.example.socialnetworking.utils.extensions.*

class EditAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditAccountBinding
    private lateinit var viewModel: EditAccountViewModel
    private lateinit var progressDialog: AlertDialog

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setListenersToView()
        subscribeObservers()
    }

    private fun init() {
        setTitleAndHomeButton(getString(R.string.edit_account))
        viewModel = ViewModelProvider(this).get(EditAccountViewModel::class.java)
        progressDialog = createProgressDialog()
    }

    private fun setListenersToView() {
        binding.btnPickImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = ALLOWED_IMAGE_MIME_TYPE
            galleryIntentResult.launch(intent)
        }

        binding.btnSave.setOnClickListener {
            viewModel.save()
        }

        binding.tiName.onTextChange(viewModel::changeName)
        binding.tiEmail.onTextChange(viewModel::changeEmail)
        binding.tiBio.onTextChange(viewModel::changeBio)
    }

    private fun subscribeObservers() {
        viewModel.accountDetailsLiveData.observe(this, Observer { accountDetails ->
            accountDetails.data?.let { details ->
                binding.tiName.editText?.setText(details.name)
                binding.tiEmail.editText?.setText(details.email)
                binding.tiBio.editText?.setText(details.bio)
                binding.profileImage.load(details.profileImageUrl, R.drawable.ic_account_circle)
            }
        })

        viewModel.selectedImageLiveData.observe(this, Observer { selectedImage ->
            binding.profileImage.load(selectedImage)
        })

        viewModel.stateLoadingLiveData.observe(this, Observer { stateLoading ->
            if (stateLoading) progressDialog.show()
            else progressDialog.dismiss()
        })

        viewModel.stateSuccessLiveData.observe(this, Observer { stateSuccess ->
            stateSuccess.data?.let { binding.root.showSnackBar(getString(R.string.msg_profile_updated)) }
        })

        viewModel.stateErrorLiveData.observe(this, Observer { stateError ->
            stateError.data?.let { binding.root.showSnackBar(it) }
        })

        viewModel.nameErrorLiveData.observe(this, Observer { errorMessage ->
            binding.tiName.setErrorMessage(errorMessage)
        })

        viewModel.bioErrorLiveData.observe(this, Observer { errorMessage ->
            binding.tiBio.setErrorMessage(errorMessage)
        })

        viewModel.emailErrorLiveData.observe(this, Observer { errorMessage ->
            binding.tiEmail.setErrorMessage(errorMessage)
        })
    }

    private val galleryIntentResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { viewModel.onGalleryIntentSuccess(it) }
            }
        }
}