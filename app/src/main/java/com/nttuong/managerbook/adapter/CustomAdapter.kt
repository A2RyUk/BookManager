package com.nttuong.managerbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.BookcaseBookItemBinding
import com.nttuong.managerbook.db.entities.Book

class CustomAdapter(
    private val listener: CustomAdapterListener
): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    private var books = arrayListOf<Book>()

    class ViewHolder(
        private val binding: BookcaseBookItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.tvBookName.text = book.name
            Glide.with(binding.imgBookAvatar)
                .load(book.avatar)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(binding.imgBookAvatar)
        }
    }

    interface CustomAdapterListener {
        fun onItemClick(book: Book)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BookcaseBookItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (books.isNotEmpty()) {
            books[position].let {
                holder.bind(it)
            }
        }
        holder.itemView.setOnClickListener {
            listener.onItemClick(books[position])
        }
    }

    override fun getItemCount(): Int = books.size

    fun updateList(myList: List<Book>) {
        if (myList.size <= 5) {
            books.clear()
            books.addAll(myList)
            notifyDataSetChanged()
        } else {
            books.clear()
            for (i in 0..4) {
                books.add(myList[i])
            }
        }
    }
}