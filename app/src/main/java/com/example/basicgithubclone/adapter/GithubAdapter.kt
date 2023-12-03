package com.example.basicgithubclone.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.basicgithubclone.databinding.ItemBindingBinding
import com.example.basicgithubclone.remote.response.PersonItemList
import com.example.basicgithubclone.ui.detail.DetailActivity
import javax.inject.Inject

class GithubAdapter @Inject constructor(): RecyclerView.Adapter<GithubAdapter.MyViewHolder>() {
    private lateinit var binding: ItemBindingBinding
    private lateinit var context: Context


    inner class MyViewHolder(private val binding: ItemBindingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PersonItemList) {
            binding.apply {
                name.text = item.login
                Glide.with(itemView)
                    .load(item.avatarUrl)
                    .circleCrop()
                    .into(circleImage)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_USERNAME, item.login)
                    intent.putExtra(DetailActivity.EXTRA_ID, item.id)
                    intent.putExtra(DetailActivity.EXTRA_AVATAR, item.avatarUrl)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val inflater = LayoutInflater.from(parent.context)
        binding = ItemBindingBinding.inflate(inflater, parent, false)
        context = parent.context
        return MyViewHolder(binding)
    }

    fun submitList(list: ArrayList<PersonItemList>) {
        Log.d("GitHubAdapter", "List size: ${list.size}")
        differ.submitList(list)
    }


    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemViewType(position: Int): Int = position


    private val differCallback = object  : DiffUtil.ItemCallback<PersonItemList>() {
        override fun areItemsTheSame(oldItem: PersonItemList, newItem: PersonItemList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PersonItemList, newItem: PersonItemList): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

}