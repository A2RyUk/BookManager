package com.nttuong.managerbook.extension

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("set_url")
fun setImageLink(imv: ImageView, link: String) {
    Glide.with(imv).load(link).into(imv)
}

@BindingAdapter("date_string")
fun setText(view: TextView, date: Date?) {
    if (date != null) {
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(date)
        view.text = currentDate
    }
}