package com.nttuong.managerbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.AuthorItemBinding
import com.nttuong.managerbook.db.entities.Author


class AuthorAdapter(
    private val authors: ArrayList<Author>
): RecyclerView.Adapter<AuthorAdapter.ViewHolder>() {
    class ViewHolder(private val binding: AuthorItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(author: Author) {
            binding.tvAuthorId.text = author.authorId.toString()
            Glide.with(binding.imgAuthorAvatar)
                .load(author.authorAvatar)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(binding.imgAuthorAvatar)
            binding.tvAuthor.text = author.authorName
            binding.tvBookNumber.text = author.numberOfBook.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AuthorItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        authors[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = authors.size

    fun updateList(myList: List<Author>)
    {
        authors.clear()
        authors.addAll(myList)
        notifyDataSetChanged()
    }
}