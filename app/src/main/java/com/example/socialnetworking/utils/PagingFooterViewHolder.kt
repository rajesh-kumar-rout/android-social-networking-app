package com.example.socialnetworking.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetworking.databinding.PagingFooterBinding
import com.example.socialnetworking.data.models.NetworkStatus

class PagingFooterViewHolder(
    private val binding: PagingFooterBinding,
    private val retryClickListener: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.btnRetry.setOnClickListener { retryClickListener }
    }

    fun bind(hasMore: Boolean, networkStatus: NetworkStatus?) {
        if (!hasMore) {
            binding.root.layoutParams.height = 0
        } else if (networkStatus == NetworkStatus.LOADING) {
            binding.progressbar.visibility = View.VISIBLE
            binding.btnRetry.visibility = View.GONE
        } else if (networkStatus == NetworkStatus.SUCCESS) {
            binding.progressbar.visibility = View.GONE
            binding.btnRetry.visibility = View.GONE
        } else if (networkStatus == NetworkStatus.FAILURE) {
            binding.progressbar.visibility = View.GONE
            binding.btnRetry.visibility = View.VISIBLE
        }
    }
}