package com.example.socialnetworking.ui.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetworking.R
import com.example.socialnetworking.data.models.Comment
import com.example.socialnetworking.data.models.PaginatedData
import com.example.socialnetworking.databinding.CommentBinding
import com.example.socialnetworking.databinding.PagingFooterBinding
import com.example.socialnetworking.utils.IMAGE_URL
import com.example.socialnetworking.utils.PagingFooterViewHolder
import com.example.socialnetworking.utils.VIEW_TYPE_DATA
import com.example.socialnetworking.utils.VIEW_TYPE_FOOTER
import com.example.socialnetworking.utils.extensions.load

class CommentAdapter(
    private val onRetryBtnClick: () -> Unit,
    private val onDeleteBtnClick: (Comment) -> Unit,
    private val paginatedComments: PaginatedData<Comment> = PaginatedData()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class CommentViewHolder(private val binding: CommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnDelete.setOnClickListener { onDeleteBtnClick(paginatedComments.data[adapterPosition]) }
        }

        fun bind(comment: Comment) {
            binding.name.text = comment.name
            binding.comment.text = comment.comment
            binding.btnDelete.visibility = if (comment.isMyComment) View.VISIBLE else View.GONE
            binding.profileImage.load(comment.profileImageUrl, R.drawable.ic_account_circle)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == paginatedComments.data.size) VIEW_TYPE_FOOTER
        else VIEW_TYPE_DATA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_FOOTER) {
            PagingFooterViewHolder(
                PagingFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onRetryBtnClick
            )
        } else {
            CommentViewHolder(
                CommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun getItemCount(): Int = paginatedComments.data.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is PagingFooterViewHolder) holder.bind(paginatedComments.hasMore, paginatedComments.networkStatus)
        if (holder is CommentViewHolder) holder.bind(paginatedComments.data[position])
    }

    fun setCommentsAndNotifyAdapter(paginatedComments: PaginatedData<Comment>){
        this.paginatedComments.copyFrom(paginatedComments)
        notifyDataSetChanged()
    }
}