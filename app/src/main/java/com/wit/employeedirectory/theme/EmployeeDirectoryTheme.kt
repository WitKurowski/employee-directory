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
	darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit
) {
	val lightColors = lightColorScheme(
		surface = colorResource(id = R.color.light_blue),
	)
	val darkColors = darkColorScheme(
		surface = colorResource(id = R.color.dark_blue),
	)
	val colors = if (darkTheme) darkColors else lightColors
	MaterialTheme(
		colorScheme = colors, content = content
	)
}