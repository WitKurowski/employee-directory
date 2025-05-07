package com.wit.employeedirectory.feature.employee

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wit.employeedirectory.R
import com.wit.employeedirectory.databinding.FragmentAllEmployeesBinding
import com.wit.employeedirectory.image.ImageLoader
import com.wit.employeedirectory.theme.EmployeeDirectoryTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@AndroidEntryPoint
class AllEmployeesFragment : Fragment() {
	@Inject
	lateinit var imageLoader: ImageLoader

	private lateinit var viewBinding: FragmentAllEmployeesBinding
	private val viewModel: AllEmployeesViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		viewBinding = FragmentAllEmployeesBinding.inflate(inflater, container, false)
		val view = viewBinding.root

		viewBinding.composeView.setContent {
			EmployeeDirectoryTheme {
				Surface(modifier = Modifier.fillMaxWidth()) {
					EmployeeList(viewModel.employeeStatesFlow, imageLoader)
					LoadingState(viewModel.loadingStateFlow)
					EmptyState(viewModel.emptyStateFlow)
					ErrorState(viewModel.errorStateFlow)
				}
			}
		}

		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewModel.fetchAllEmployees()

		with(viewBinding) {
			ViewCompat.setOnApplyWindowInsetsListener(appBar) { _, windowInsets ->
				val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
				view.setPadding(
					insets.left, insets.top, insets.right, view.paddingBottom
				)
				WindowInsetsCompat.CONSUMED
			}

			toolbar.title = getString(R.string.app_name)
		}

		setUpMenu()
	}

	private fun setUpMenu() {
		viewBinding.toolbar.addMenuProvider(object : MenuProvider {
			override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
				menuInflater.inflate(R.menu.fragment_all_employees, menu)
			}

			override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
				val result = when (menuItem.itemId) {
					R.id.refresh -> {
						viewModel.fetchAllEmployees(forceRefresh = true)

						true
					}

					R.id.sort -> {
						SortOptionsDialogFragment().show(childFragmentManager, null)

						true
					}

					else -> false
				}

				return result
			}
		}, viewLifecycleOwner, Lifecycle.State.RESUMED)
	}

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
}

@Composable
private fun EmployeeList(
	employeeStatesFlow: StateFlow<List<EmployeeState>>, imageLoader: ImageLoader
) {
	val employees by employeeStatesFlow.collectAsStateWithLifecycle()

	// TODO: Fix top window insets for list in landscape orientation.
	LazyColumn(
		contentPadding = WindowInsets.systemBars.asPaddingValues(),
		modifier = Modifier
			.fillMaxSize()
			.windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Start))
	) {
		items(employees) {
			EmployeeListItem(imageLoader, it.name, it.photoUrlString, it.team)
		}
	}
}

@Composable
private fun EmployeeListItem(
	imageLoader: ImageLoader,
	@PreviewParameter(LoremIpsum::class) name: String,
	@PreviewParameter(LoremIpsum::class) photoUrlString: String,
	@PreviewParameter(LoremIpsum::class) team: String
) {
	Row(
		modifier = Modifier.padding(
			horizontal = dimensionResource(R.dimen.screen_horizontal_spacing),
			vertical = dimensionResource(R.dimen.screen_vertical_spacing)
		), verticalAlignment = Alignment.CenterVertically
	) {
		imageLoader.Image(
			contentDescriptionResId = R.string.photo,
			modifier = Modifier.size(64.dp),
			placeholderDrawableResId = R.drawable.baseline_person_24,
			urlString = photoUrlString
		)
		Spacer(modifier = Modifier.width(8.dp))
		Column {
			Text(
				maxLines = 1,
				overflow = TextOverflow.Ellipsis,
				style = MaterialTheme.typography.titleLarge,
				text = name
			)
			Text(
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis,
				style = MaterialTheme.typography.bodyMedium,
				text = team
			)
		}
	}
}

@Composable
private fun EmptyState(emptyStateFlow: StateFlow<EmptyState>) {
	val emptyState by emptyStateFlow.collectAsStateWithLifecycle()

	if (emptyState.visible) {
		Surface(color = Color.Transparent) {
			Text(
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.wrapContentWidth(),
				style = MaterialTheme.typography.bodyLarge,
				text = stringResource(R.string.no_employees_found)
			)
		}
	}
}

@Composable
private fun ErrorState(errorStateFlow: StateFlow<ErrorState>) {
	val errorState by errorStateFlow.collectAsStateWithLifecycle()

	if (errorState.visible) {
		Surface(color = Color.Transparent) {
			Column {
				Image(
					contentDescription = null,
					imageVector = ImageVector.vectorResource(R.drawable.baseline_error_outline_24),
					modifier = Modifier.align(Alignment.CenterHorizontally)
				)
				Spacer(modifier = Modifier.height(8.dp))
				Text(
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					modifier = Modifier.wrapContentWidth(),
					style = MaterialTheme.typography.bodyLarge,
					text = stringResource(R.string.failed_to_retrieve_employees)
				)
			}
		}
	}
}

@Composable
private fun LoadingState(loadingStateFlow: StateFlow<LoadingState>) {
	val loadingState by loadingStateFlow.collectAsStateWithLifecycle()

	if (loadingState.visible) {
		// TODO: Fix top insets after app bar layout is migrated to Compose.
		LinearProgressIndicator(modifier = Modifier.safeContentPadding())
	}
}