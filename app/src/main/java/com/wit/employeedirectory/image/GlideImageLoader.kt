package com.wit.employeedirectory.image

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GlideImageLoader @Inject constructor(@ApplicationContext private val context: Context) :
	ImageLoader {
	override fun load(imageView: ImageView, urlString: String, placeholderDrawableRes: Int) {
		Glide.with(context) //
			.load(urlString) //
			.placeholder(placeholderDrawableRes) //
			.into(imageView)
	}
}