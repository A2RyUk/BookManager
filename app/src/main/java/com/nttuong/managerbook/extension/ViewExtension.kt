package com.nttuong.managerbook.extension

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("set_url")
fun setImageLink(imv: ImageView, link: String) {
    Glide.with(imv).load(link).into(imv)
}