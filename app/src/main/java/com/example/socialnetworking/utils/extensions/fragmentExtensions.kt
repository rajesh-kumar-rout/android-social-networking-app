package com.example.socialnetworking.utils.extensions

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.socialnetworking.databinding.DialogLoadingBinding

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.createProgressDialog(): AlertDialog {
    return AlertDialog.Builder(requireContext())
        .setView(DialogLoadingBinding.inflate(layoutInflater).root)
        .setCancelable(false)
        .create()
}



