package com.nttuong.managerbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nttuong.managerbook.databinding.ChapterItemBinding
import com.nttuong.managerbook.db.entities.Chapter

class ChapterAdapter(
    private val chapters: ArrayList<Chapter>
): RecyclerView.Adapter<ChapterAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ChapterItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chapter: Chapter) {
            binding.tvChapName.text = chapter.chapName
            binding.tvChapNumber.text = chapter.chapNumber.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ChapterAdapter.ViewHolder(
            ChapterItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        chapters[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = chapters.size
}