package com.wit.employeedirectory.feature.employee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
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

		// TODO: Find proper way to handle insets and padding when dealing with edge-to-edge.
		viewBinding.composeView.setContent {
			EmployeeDirectoryTheme {
				Scaffold(topBar = {
					TopAppBar(childFragmentManager, viewModel)
				}) {
					Surface(
						modifier = Modifier
							.fillMaxWidth()
							.padding(it)
					) {
						EmployeeList(viewModel.employeeStatesFlow, imageLoader)
						LoadingState(viewModel.loadingStateFlow)
						EmptyState(viewModel.emptyStateFlow)
						ErrorState(viewModel.errorStateFlow)
					}
				}
			}
		}

		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewModel.fetchAllEmployees()
	}
}

@Composable
private fun EmployeeList(
	employeeStatesFlow: StateFlow<List<EmployeeState>>, imageLoader: ImageLoader
) {
	val employees by employeeStatesFlow.collectAsStateWithLifecycle()

	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal))
	) {
		items(employees, key = { it.id }) {
			EmployeeListItem(
				imageLoader, Modifier.animateItem(), it.name, it.photoUrlString, it.team
			)
		}
	}
}

@Composable
private fun EmployeeListItem(
	imageLoader: ImageLoader,
	modifier: Modifier,
	@PreviewParameter(LoremIpsum::class) name: String,
	@PreviewParameter(LoremIpsum::class) photoUrlString: String,
	@PreviewParameter(LoremIpsum::class) team: String
) {
	Row(
		modifier = modifier.padding(
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
		LinearProgressIndicator()
	}
}

@Composable
private fun TopAppBar(childFragmentManager: FragmentManager, viewModel: AllEmployeesViewModel) {
	TopAppBar(actions = {
		IconButton(
			onClick = {
				SortOptionsDialogFragment().show(childFragmentManager, null)
			}) {
			Image(
				contentDescription = stringResource(R.string.sort),
				imageVector = ImageVector.vectorResource(R.drawable.baseline_sort_24)
			)
		}
		IconButton(
			onClick = { viewModel.fetchAllEmployees(forceRefresh = true) }) {
			Image(
				contentDescription = stringResource(R.string.refresh),
				imageVector = ImageVector.vectorResource(R.drawable.baseline_refresh_24)
			)
		}
	}, title = { Text(text = stringResource(R.string.app_name)) })
}