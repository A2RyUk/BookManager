package com.nttuong.managerbook.extension

import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@BindingAdapter("set_url")
fun setImageLink(imv: ImageView, link: String) {
    Glide.with(imv).load(link).into(imv)
}

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("local_date_time_string")
fun setText(view: TextView, date: LocalDateTime?) {
    if (date != null) {
        val currentDate: String = date.format(
            DateTimeFormatter
                .ofPattern("dd/MM/yyyy HH:mm:ss")
        )
        view.text = currentDate
    }
}