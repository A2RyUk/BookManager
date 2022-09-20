package com.nttuong.managerbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.CategoryItemBinding
import com.nttuong.managerbook.db.entities.Author
import com.nttuong.managerbook.db.entities.Category

class CategoryAdapter(
    private val categories: ArrayList<Category>
): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    class ViewHolder(private val binding: CategoryItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            Glide.with(binding.imgAvatar)
                .load(category.categoryAvatar)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(binding.imgAvatar)
            binding.tvCategoryName.text = category.categoryName
            binding.tvBookNumber.text = "Số sách: " + category.bookNumber.toString() + " quyển"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        categories[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = categories.size

    fun updateList(myList: List<Category>)
    {
        categories.clear()
        categories.addAll(myList)
        notifyDataSetChanged()
    }
}