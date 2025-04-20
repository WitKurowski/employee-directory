package com.wit.employeedirectory.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.wit.employeedirectory.R

@Composable
fun EmployeeDirectoryTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	content: @Composable () -> Unit
) {
	val lightColors = lightColorScheme(
		primary = colorResource(id = R.color.light_blue),
		secondary = colorResource(id = R.color.teal_200),
		onPrimary = colorResource(id = R.color.white),
		onSecondary = colorResource(id = R.color.black),
	)
	val darkColors = darkColorScheme(
		primary = colorResource(id = R.color.purple_200),
		secondary = colorResource(id = R.color.teal_200),
		onPrimary = colorResource(id = R.color.black),
		onSecondary = colorResource(id = R.color.black),
	)
	val colors = if (darkTheme) darkColors else lightColors
	MaterialTheme(
		colorScheme = colors,
		content = content
	)
}