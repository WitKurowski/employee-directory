package com.wit.employeedirectory.image

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface ImageLoader {
	@Composable
	fun Image(
		@StringRes contentDescriptionResId: Int,
		modifier: Modifier,
		@DrawableRes placeholderDrawableResId: Int,
		urlString: String
	)
}