package com.memo.mynotes.utils

import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.memo.mynotes.R

class ImageBindingAdapters {
    companion object {
        @JvmStatic @BindingAdapter("imageResource")
        fun setImageResource(view: ImageView, imageUrl: Int){
            val context = view.context

            val options = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(context)
                .setDefaultRequestOptions(options)
                .load(imageUrl)
                .into(view)
        }
    }
}