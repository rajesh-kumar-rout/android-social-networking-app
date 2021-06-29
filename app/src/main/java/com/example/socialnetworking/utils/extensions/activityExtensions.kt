package com.example.socialnetworking.utils.extensions

import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.socialnetworking.databinding.DialogLoadingBinding

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.setTitleAndHomeButton(title: String) {
    this.title = title
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
}

fun Activity.createProgressDialog(): AlertDialog {
    return AlertDialog.Builder(this)
        .setView(DialogLoadingBinding.inflate(layoutInflater).root)
        .setCancelable(false)
        .create()
}
