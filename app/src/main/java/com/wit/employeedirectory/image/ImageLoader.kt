package com.wit.employeedirectory.image

import android.widget.ImageView
import androidx.annotation.DrawableRes

interface ImageLoader {
	fun load(imageView: ImageView, urlString: String, @DrawableRes placeholderDrawableRes: Int)
}