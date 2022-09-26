package com.nttuong.managerbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nttuong.managerbook.databinding.ChapterItemBinding
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.db.entities.Chapter

class ChapterAdapter(
    private var chapterItemClickListener: ChapterItemClickListener
): RecyclerView.Adapter<ChapterAdapter.ViewHolder>() {

    private val chapters = arrayListOf<Chapter>()

    class ViewHolder(private val binding: ChapterItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chapter: Chapter) {
            binding.tvChapName.text = chapter.chapName
            binding.tvChapNumber.text = chapter.chapNumber.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ChapterItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        chapters[position].let {
            holder.bind(it)
        }
        holder.itemView.setOnClickListener {
            chapterItemClickListener.itemClick(chapters[position])
        }
    }

    override fun getItemCount(): Int = chapters.size

    fun updateUI(myList: List<Chapter>) {
        chapters.clear()
        chapters.addAll(myList)
        notifyDataSetChanged()
    }
}

interface ChapterItemClickListener {
    fun itemClick(chapter: Chapter)
}