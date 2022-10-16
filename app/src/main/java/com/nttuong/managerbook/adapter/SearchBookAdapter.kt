package com.nttuong.managerbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nttuong.managerbook.databinding.ItemBookSearchViewBinding
import com.nttuong.managerbook.db.entities.Book

val searchDiff = object : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.bookId == newItem.bookId
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }
}

class SearchBookAdapter() : ListAdapter<Book, SearchBookAdapter.ViewHolder>(searchDiff) {
    class ViewHolder(
        private val binding: ItemBookSearchViewBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.book = book
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBookSearchViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}