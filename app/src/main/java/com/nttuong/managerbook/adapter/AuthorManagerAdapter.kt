package com.nttuong.managerbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nttuong.managerbook.databinding.AuthorItemBinding
import com.nttuong.managerbook.db.entities.Author

val authorManagerDiff = object : DiffUtil.ItemCallback<Author>() {
    override fun areItemsTheSame(oldItem: Author, newItem: Author): Boolean {
        return oldItem.authorId == newItem.authorId
    }

    override fun areContentsTheSame(oldItem: Author, newItem: Author): Boolean {
        return oldItem == newItem
    }

}

class AuthorManagerAdapter() : ListAdapter<Author, AuthorManagerAdapter.ViewHolder>(authorManagerDiff) {
    class ViewHolder(
        private val binding: AuthorItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(author: Author) {
            binding.author = author
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AuthorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}