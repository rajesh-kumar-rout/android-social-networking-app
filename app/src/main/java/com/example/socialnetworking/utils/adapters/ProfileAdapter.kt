package com.example.socialnetworking.utils.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialnetworking.R
import com.example.socialnetworking.data.models.PaginatedData
import com.example.socialnetworking.databinding.ProfileBinding
import com.example.socialnetworking.data.models.Profile
import com.example.socialnetworking.databinding.PagingFooterBinding
import com.example.socialnetworking.utils.PagingFooterViewHolder
import com.example.socialnetworking.utils.VIEW_TYPE_DATA
import com.example.socialnetworking.utils.VIEW_TYPE_FOOTER
import com.example.socialnetworking.utils.extensions.load

class ProfileAdapter(
    private val onRetryBtnClick: () -> Unit,
    private val onProfileClick: (position: Int) -> Unit,
    private val paginatedProfiles: PaginatedData<Profile> = PaginatedData()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ProfileViewHolder(private val binding: ProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { onProfileClick(paginatedProfiles.data[adapterPosition].id) }
        }

        fun bind(profile: Profile) {
            binding.profileImage.load(profile.profileImageUrl, R.drawable.ic_account_circle)
            binding.name.text = profile.name
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == paginatedProfiles.data.size) VIEW_TYPE_FOOTER
        else VIEW_TYPE_DATA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_FOOTER) {
            PagingFooterViewHolder(
                PagingFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onRetryBtnClick
            )
        } else {
            ProfileViewHolder(
                ProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun getItemCount(): Int = paginatedProfiles.data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is PagingFooterViewHolder) holder.bind(paginatedProfiles.hasMore, paginatedProfiles.networkStatus)
        if (holder is ProfileViewHolder) holder.bind(paginatedProfiles.data[position])
    }

    fun setProfilesAndNotifyAdapter(paginatedProfiles: PaginatedData<Profile>){
        this.paginatedProfiles.copyFrom(paginatedProfiles)
        notifyDataSetChanged()
    }
}