package com.memo.mynotes.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.memo.mynotes.R

class BindingAdapters {
    companion object {
        @JvmStatic
        @BindingAdapter("imageResource")
        fun setImageResource(view: ImageView, imageUrl: Int) {
            val context = view.context

            val options = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(context)
                .setDefaultRequestOptions(options)
                .load(imageUrl)
                .into(view)
        }

        @JvmStatic
        @BindingAdapter("textColor")
        fun setTextColor(view: TextView, bgColor: Int) {
            if (bgColor == AppData.BLUE || bgColor == AppData.GREEN) {
                view.setTextColor(AppData.WHITE)
            } else {
                view.setTextColor(AppData.BLACK)
            }
        }
    }
}