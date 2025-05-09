package com.wit.employeedirectory.util

import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun dimensionResourceOrZero(@DimenRes resId: Int?): Dp {
	return if (resId == null) {
		0.dp
	} else {
		dimensionResource(resId)
	}
}