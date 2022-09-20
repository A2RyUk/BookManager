package com.nttuong.managerbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.BookItemBinding
import com.nttuong.managerbook.db.entities.Book

class BookAdapter(
    private val books: ArrayList<Book>,
    private var itemClickListener: ItemClickListener
): RecyclerView.Adapter<BookAdapter.ViewHolder>() {
    class ViewHolder(private val binding: BookItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.tvId.text = book.bookId.toString()
            Glide.with(binding.imgAva)
                .load(book.avatar)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(binding.imgAva)
            binding.tvName.text = book.name
            binding.tvAuthor.text = book.author
            binding.tvCategory.text = book.category
            binding.tvStatus.text = book.status
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BookItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        books[position].let {
            holder.bind(it)
        }
        holder.itemView.setOnClickListener {
            itemClickListener.itemClick(books[position])
        }
    }

    override fun getItemCount(): Int = books.size

    fun updateList(myList: List<Book>)
    {
        books.clear()
        books.addAll(myList)
        notifyDataSetChanged()
    }
}

interface ItemClickListener {
    fun itemClick(book: Book)
}
