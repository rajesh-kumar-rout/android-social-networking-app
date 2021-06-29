package com.example.socialnetworking.utils.extensions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.Placeholder
import com.bumptech.glide.Glide
import com.example.socialnetworking.R
import com.example.socialnetworking.utils.IMAGE_URL
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import java.io.File

fun TextInputLayout.onTextChange(onTextChangeListener: (String) -> Unit){
    editText?.addTextChangedListener(object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChangeListener.invoke(s.toString())
        }
    })
}

fun TextInputLayout.setErrorMessage(errorMessage: String){
    this.isErrorEnabled = errorMessage.isNotEmpty()
    error = errorMessage
}

fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun ImageView.load(imageUrl: String?, placeholder: Int = R.drawable.image_bg) {
    Glide.with(this.context).load(IMAGE_URL + imageUrl).placeholder(placeholder).into(this)
}

fun ImageView.load(image: File, placeholder: Int = R.drawable.image_bg) {
    Glide.with(this.context).load(image).placeholder(placeholder).into(this)
}