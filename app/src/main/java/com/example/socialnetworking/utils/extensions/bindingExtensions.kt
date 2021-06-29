package com.example.socialnetworking.utils.extensions

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetworking.databinding.LayoutRecyclerviewBinding
import com.example.socialnetworking.utils.RecyclerViewScrollListener

fun LayoutRecyclerviewBinding.error(message: String) {
    layoutProgressbar.visibility = View.GONE
    layoutError.visibility = View.VISIBLE
    tvEmpty.visibility = View.GONE
    recyclerview.visibility = View.GONE
    tvError.text = message
}

fun LayoutRecyclerviewBinding.empty(message: String) {
    layoutProgressbar.visibility = View.GONE
    layoutError.visibility = View.GONE
    tvEmpty.visibility = View.VISIBLE
    recyclerview.visibility = View.GONE
    tvEmpty.text = message
}

fun LayoutRecyclerviewBinding.hasData() {
    layoutProgressbar.visibility = View.GONE
    layoutError.visibility = View.GONE
    tvEmpty.visibility = View.GONE
    recyclerview.visibility = View.VISIBLE
}

fun LayoutRecyclerviewBinding.loading() {
    layoutProgressbar.visibility = View.VISIBLE
    layoutError.visibility = View.GONE
    tvEmpty.visibility = View.GONE
    recyclerview.visibility = View.GONE
}

fun LayoutRecyclerviewBinding.init(
    context: Context,
    adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
    onRetryClick: () -> Unit
) {
    val layoutManager = LinearLayoutManager(context)
    recyclerview.adapter = adapter
    recyclerview.layoutManager = layoutManager
    recyclerview.addOnScrollListener(RecyclerViewScrollListener(layoutManager, onRetryClick))
    btnRetry.setOnClickListener { onRetryClick.invoke() }
}