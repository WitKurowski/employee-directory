package com.wit.employeedirectory.feature.employee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wit.employeedirectory.databinding.EmployeeListItemBinding
import com.wit.employeedirectory.databinding.FragmentAllEmployeesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllEmployeesFragment : Fragment() {
	private val employeesListAdapter = EmployeesListAdapter()
	private lateinit var viewBinding: FragmentAllEmployeesBinding
	private val viewModel: AllEmployeesViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		viewBinding = FragmentAllEmployeesBinding.inflate(inflater, container, false)
		val view = viewBinding.root

		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewModel.fetchAllEmployees()

		with(viewBinding.employees) {
			// TODO: Consider using setHasFixedSize()
			adapter = employeesListAdapter
			layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		}

		viewLifecycleOwner.lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.RESUMED) {
				viewModel.employeeStatesFlow.collect {
					employeesListAdapter.submitList(it)
				}
			}
		}
	}

	private class EmployeesListAdapter :
		ListAdapter<EmployeeState, EmployeesListAdapter.EmployeeViewHolder>(
			EmployeeStateItemCallback()
		) {
		override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
			val employeeState = getItem(position)

			with(holder.employeeListItemBinding) {
				name.text = employeeState.name

				Glide.with(photo.context) //
					.load(employeeState.photoUrlString) //
					.into(photo)
			}
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
			val context = parent.context
			val layoutInflater = LayoutInflater.from(context)
			val employeeListItemBinding =
				EmployeeListItemBinding.inflate(layoutInflater, parent, false)
			val employeeViewHolder = EmployeeViewHolder(employeeListItemBinding)

			return employeeViewHolder
		}

		private class EmployeeStateItemCallback : DiffUtil.ItemCallback<EmployeeState>() {
			override fun areContentsTheSame(
				oldItem: EmployeeState, newItem: EmployeeState
			): Boolean = oldItem == newItem

			override fun areItemsTheSame(oldItem: EmployeeState, newItem: EmployeeState): Boolean =
				oldItem.id == newItem.id
		}

		private class EmployeeViewHolder(val employeeListItemBinding: EmployeeListItemBinding) :
			RecyclerView.ViewHolder(employeeListItemBinding.root)
	}
}