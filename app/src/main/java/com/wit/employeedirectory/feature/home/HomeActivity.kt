package com.wit.employeedirectory.feature.home

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.wit.employeedirectory.R

class HomeActivity : FragmentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContentView(R.layout.activity_home)
	}
}