package com.wit.employeedirectory.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.wit.employeedirectory.R

@Composable
fun EmployeeDirectoryTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	dynamicColor: Boolean = true,
	content: @Composable () -> Unit
) {
	val colorScheme = when {
		dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
			val context = LocalContext.current
			if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
		}

		darkTheme -> {
			darkColorScheme(
				primary = colorResource(R.color.dark_blue),
				secondary = colorResource(R.color.light_indigo),
				tertiary = colorResource(R.color.light_purple),
				surface = colorResource(R.color.dark_indigo),
			)
		}

		else -> {
			lightColorScheme(
				primary = colorResource(R.color.indigo),
				secondary = colorResource(R.color.slightly_lighter_indigo),
				tertiary = colorResource(R.color.deep_purple),
				surface = colorResource(R.color.light_lavender),
			)
		}
	}
	MaterialTheme(
		colorScheme = colorScheme, content = content
	)
}