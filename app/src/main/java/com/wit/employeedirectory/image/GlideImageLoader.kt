package com.wit.employeedirectory.image

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import javax.inject.Inject

class GlideImageLoader @Inject constructor() : ImageLoader {
	@Composable
	override fun Image(
		@StringRes contentDescriptionResId: Int,
		modifier: Modifier,
		@DrawableRes placeholderDrawableResId: Int,
		urlString: String
	) {
		GlideImage(
			contentDescription = stringResource(contentDescriptionResId),
			failure = placeholder(placeholderDrawableResId),
			loading = placeholder(placeholderDrawableResId),
			model = urlString,
			modifier = modifier
		)
	}
}