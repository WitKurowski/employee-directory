package com.wit.employeedirectory.extension

import androidx.annotation.DimenRes
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wit.employeedirectory.util.dimensionResourceOrZero

@Composable
fun Modifier.padding(bottom: Dp = 0.dp, horizontal: Dp, top: Dp = 0.dp): Modifier {
	return padding(bottom = bottom, end = horizontal, start = horizontal, top = top)
}

@Composable
fun Modifier.padding(@DimenRes horizontal: Int? = null, @DimenRes vertical: Int? = null): Modifier {
	return padding(
		horizontal = dimensionResourceOrZero(horizontal),
		vertical = dimensionResourceOrZero(vertical)
	)
}