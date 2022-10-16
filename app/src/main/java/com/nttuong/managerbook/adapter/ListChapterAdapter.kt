package com.nttuong.managerbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nttuong.managerbook.databinding.ChapterItemBinding
import com.nttuong.managerbook.db.entities.Chapter

val chapterManagerDiff = object : DiffUtil.ItemCallback<Chapter>(){
    override fun areItemsTheSame(oldItem: Chapter, newItem: Chapter): Boolean {
        return oldItem.chapterId == newItem.chapterId
    }

    override fun areContentsTheSame(oldItem: Chapter, newItem: Chapter): Boolean {
        return oldItem == newItem
    }

}

class ListChapterAdapter(
    private var chapterItemClickListener: ChapterItemClickListener
): ListAdapter<Chapter, ListChapterAdapter.ViewHolder>(chapterManagerDiff) {
    class ViewHolder(
        private val binding: ChapterItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(chapter: Chapter) {
            binding.chapter = chapter
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ChapterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
        holder.itemView.setOnClickListener {
            chapterItemClickListener.itemClick(getItem(position))
        }
    }
}

interface ChapterItemClickListener {
    fun itemClick(chapter: Chapter)
}