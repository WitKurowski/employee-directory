package com.wit.employeedirectory.feature.employee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wit.employeedirectory.R
import com.wit.employeedirectory.databinding.EmployeeListItemBinding
import com.wit.employeedirectory.databinding.FragmentAllEmployeesBinding
import com.wit.employeedirectory.image.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AllEmployeesFragment : Fragment() {
	@Inject
	lateinit var employeesListAdapter: EmployeesListAdapter

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

		with(viewBinding) {
			with(employees) {
				// TODO: Consider using setHasFixedSize()
				adapter = employeesListAdapter
				layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

				ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
					val insets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
					view.setPadding(
						view.paddingLeft, view.paddingTop, view.paddingRight, insets.bottom
					)
					WindowInsetsCompat.CONSUMED
				}
			}

			toolbar.title = getString(R.string.employees)
		}

		setUpMenu()

		viewLifecycleOwner.lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.RESUMED) {
				launch {
					viewModel.employeeStatesFlow.collect {
						employeesListAdapter.submitList(it)
					}
				}

				launch {
					viewModel.emptyStateFlow.collect {
						viewBinding.emptyStateMessage.isVisible = it.visible
					}
				}

				launch {
					viewModel.errorStateFlow.collect {
						viewBinding.errorView.isVisible = it.visible
					}
				}

				launch {
					viewModel.loadingStateFlow.collect {
						viewBinding.loadingProgressIndicator.isVisible = it.visible
					}
				}
			}
		}
	}

	private fun setUpMenu() {
		viewBinding.toolbar.addMenuProvider(object : MenuProvider {
			override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
				menuInflater.inflate(R.menu.fragment_all_employees, menu)
			}

			override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
				val result = when (menuItem.itemId) {
					R.id.refresh -> {
						viewModel.fetchAllEmployees()
						true
					}

					else -> false
				}

				return result
			}
		}, viewLifecycleOwner, Lifecycle.State.RESUMED)
	}

	class EmployeesListAdapter @Inject constructor(private val imageLoader: ImageLoader) :
		ListAdapter<EmployeeState, EmployeesListAdapter.EmployeeViewHolder>(
			EmployeeStateItemCallback()
		) {
		override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
			val employeeState = getItem(position)

			with(holder.employeeListItemBinding) {
				name.text = employeeState.name
				imageLoader.load(photo, employeeState.photoUrlString, R.drawable.baseline_person_24)
				team.text = employeeState.team
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

		class EmployeeViewHolder(val employeeListItemBinding: EmployeeListItemBinding) :
			RecyclerView.ViewHolder(employeeListItemBinding.root)
	}
}