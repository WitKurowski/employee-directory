package com.wit.employeedirectory.feature.home

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import com.wit.employeedirectory.databinding.ActivityHomeBinding
import com.wit.employeedirectory.feature.employee.AllEmployeesScreen
import com.wit.employeedirectory.theme.EmployeeDirectoryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : FragmentActivity() {
	private lateinit var viewBinding: ActivityHomeBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		enableEdgeToEdge()

		viewBinding = ActivityHomeBinding.inflate(layoutInflater)

		setContentView(viewBinding.root)

		// TODO: Find proper way to handle insets and padding when dealing with edge-to-edge.
		viewBinding.composeView.setContent {
			EmployeeDirectoryTheme {
				AllEmployeesScreen()
			}
		}
	}
}