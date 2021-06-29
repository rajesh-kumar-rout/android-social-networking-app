package com.example.socialnetworking.utils.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetworking.R
import com.example.socialnetworking.data.models.PaginatedData
import com.example.socialnetworking.data.models.Post
import com.example.socialnetworking.databinding.PagingFooterBinding
import com.example.socialnetworking.databinding.PostBinding
import com.example.socialnetworking.utils.*
import com.example.socialnetworking.utils.extensions.load

class PostAdapter(
    private val retryClickListener: () -> Unit,
    private val onCommentBtnClick: (Int) -> Unit,
    private val context: Context,
    private val onDeleteBtnClick: ((Post) -> Unit)? = null,
    private val onLikeBtnClick: ((Post) -> Unit)? = null,
    private val paginatedPosts: PaginatedData<Post> = PaginatedData()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class PostViewHolder(private val binding: PostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnDelete.visibility = if(onDeleteBtnClick != null) View.VISIBLE else View.GONE
            binding.btnDelete.setOnClickListener { onDeleteBtnClick?.invoke(paginatedPosts.data[adapterPosition]) }
            binding.btnComment.setOnClickListener { onCommentBtnClick(paginatedPosts.data[adapterPosition].id) }
            binding.btnLike.setOnClickListener { onLikeBtnClick?.invoke(paginatedPosts.data[adapterPosition]) }
        }

        fun bind(post: Post) {
            binding.postImage.load(post.postImageUrl)
            binding.profileImage.load(post.profileImageUrl, R.drawable.ic_account_circle)
            binding.name.text = post.name
            binding.createdAt.text = post.createdAt
            binding.description.visibility = if(post.description.isNotEmpty()) View.VISIBLE else View.GONE
            binding.description.text = post.description
            binding.likesComment.text = "${post.totalLikes} likes"
            val drawableId = if (post.isLiked) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            val imageDrawable = ResourcesCompat.getDrawable(context.resources, drawableId, null)
            binding.btnLike.setImageDrawable(imageDrawable)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == paginatedPosts.data.size) VIEW_TYPE_FOOTER
        else VIEW_TYPE_DATA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_FOOTER) {
            PagingFooterViewHolder(
                PagingFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                retryClickListener
            )
        } else {
            PostViewHolder(
                PostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun getItemCount(): Int = paginatedPosts.data.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is PagingFooterViewHolder) holder.bind(paginatedPosts.hasMore, paginatedPosts.networkStatus)
        if (holder is PostViewHolder) holder.bind(paginatedPosts.data[position])
    }

    fun setPostsAndNotifyAdapter(paginatedData: PaginatedData<Post>){
        this.paginatedPosts.copyFrom(paginatedData)
        notifyDataSetChanged()
    }
}
