package com.example.socialnetworking.ui.addPost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.socialnetworking.databinding.DialogPickImageBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheet : BottomSheetDialogFragment(){

    private lateinit var viewModel: AddPostViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogPickImageBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(AddPostViewModel::class.java)

        binding.pickCamera.setOnClickListener {
            viewModel.pickImageFromCamera()
            dismiss()
        }

        binding.pickGallery.setOnClickListener {
            viewModel.pickImageFromGallery()
            dismiss()
        }

        return binding.root
    }
}