package com.example.socialnetworking.ui.addPost

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.socialnetworking.R
import com.example.socialnetworking.databinding.DialogLoadingBinding
import com.example.socialnetworking.databinding.FragmentAddPostBinding
import com.example.socialnetworking.utils.ALLOWED_IMAGE_MIME_TYPE
import com.example.socialnetworking.utils.extensions.createProgressDialog
import com.example.socialnetworking.utils.extensions.load
import com.example.socialnetworking.utils.extensions.showSnackBar

class AddPostFragment : Fragment(R.layout.fragment_add_post) {

    private lateinit var binding: FragmentAddPostBinding
    private lateinit var viewModel: AddPostViewModel
    private lateinit var progressDialog: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        setListenerToViews()
        subscribeObservers()
    }

    private fun init(view: View) {
        binding = FragmentAddPostBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity()).get(AddPostViewModel::class.java)
        binding.appbar.toolbar.title = getString(R.string.add_post)
        binding.appbar.toolbar.inflateMenu(R.menu.menu_add_post)
        binding.appbar.toolbar.setOnMenuItemClickListener(this::onMenuItemClickListener)
        progressDialog = createProgressDialog()
    }

    private fun onMenuItemClickListener(menuItem: MenuItem): Boolean{
        if(menuItem.itemId == R.id.add_image) BottomSheet().show(childFragmentManager, null)
        return true
    }

    private fun setListenerToViews() {
        binding.btnUpload.setOnClickListener {
            viewModel.upload(binding.description.text.toString())
        }
    }

    private fun subscribeObservers() {
        viewModel.selectedImageLiveData.observe(viewLifecycleOwner, Observer {
            binding.tvNoImage.visibility = View.GONE
            binding.imageCard.visibility = View.VISIBLE
            binding.imageView.load(it)
        })

        viewModel.stateLoadingLiveData.observe(viewLifecycleOwner, Observer { isLoading ->
            if(isLoading) progressDialog.show()
            else progressDialog.dismiss()
        })

        viewModel.stateErrorLiveData.observe(viewLifecycleOwner, Observer { isError ->
            isError.data?.let { binding.rootLayout.showSnackBar(it) }
        })

        viewModel.stateSuccessLiveData.observe(viewLifecycleOwner, Observer { isSuccess ->
            isSuccess.data?.let { binding.rootLayout.showSnackBar("Post Added Successfully") }
        })

        viewModel.pickImageFromGalleryLiveData.observe(viewLifecycleOwner, Observer { pickImage ->
            pickImage.data?.let { _ ->
                val intent = Intent()
                intent.action = Intent.ACTION_PICK
                intent.type = ALLOWED_IMAGE_MIME_TYPE
                galleryIntentResult.launch(intent)
            }
        })

        viewModel.pickImageFromCameraLiveData.observe(viewLifecycleOwner, Observer { pickImage ->
            pickImage.data?.let { uri ->
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                cameraIntentResult.launch(intent)
            }
        })
    }

    private val galleryIntentResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { viewModel.onGalleryIntentSuccess(it) }
            }
        }

    private val cameraIntentResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) viewModel.onCameraIntentSuccess()
            else viewModel.onCameraIntentCanceled()
        }
}