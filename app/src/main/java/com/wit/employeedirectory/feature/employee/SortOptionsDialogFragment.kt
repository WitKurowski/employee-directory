package com.wit.employeedirectory.feature.employee

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.wit.employeedirectory.R

class SortOptionsDialogFragment : DialogFragment() {
	private val viewModel: AllEmployeesViewModel by viewModels(ownerProducer = { requireParentFragment() })

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val alertDialog = AlertDialog.Builder(requireContext()).run {
			setItems(R.array.sort_options) { _, which ->
				when (which) {
					0 -> viewModel.sortSelected(SortOption.NAME)
					1 -> viewModel.sortSelected(SortOption.TEAM)
				}
			}
			setTitle(R.string.sort_by)

			create()
		}

		return alertDialog
	}
}