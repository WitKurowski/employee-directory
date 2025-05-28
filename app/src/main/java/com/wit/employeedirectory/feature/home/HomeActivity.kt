package com.wit.employeedirectory.feature.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wit.employeedirectory.feature.employee.AllEmployeesScreen
import com.wit.employeedirectory.theme.EmployeeDirectoryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		enableEdgeToEdge()

		// TODO: Find proper way to handle insets and padding when dealing with edge-to-edge.
		setContent {
			EmployeeDirectoryTheme {
				AllEmployeesScreen()
			}
		}
	}
}