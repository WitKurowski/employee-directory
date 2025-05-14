package com.wit.employeedirectory.feature.employee

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wit.employeedirectory.R
import com.wit.employeedirectory.extension.padding
import com.wit.employeedirectory.image.ImageLoader
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AllEmployeesScreen(viewModel: AllEmployeesViewModel = viewModel()) {
	val sortDialogVisible by viewModel.sortDialogVisibleStateFlow.collectAsStateWithLifecycle()

	if (sortDialogVisible) {
		SortOptionsDialog(viewModel)
	}

	val loadData = remember { true }

	LaunchedEffect(loadData) {
		if (loadData) {
			viewModel.fetchAllEmployees()
		}
	}

	Scaffold(topBar = {
		TopAppBar(viewModel)
	}) {
		Surface(
			modifier = Modifier
				.fillMaxWidth()
				.padding(it)
		) {
			EmployeeList(viewModel.employeeStatesFlow, viewModel.imageLoader)
			LoadingState(viewModel.loadingStateFlow)
			EmptyState(viewModel.emptyStateFlow)
			ErrorState(viewModel.errorStateFlow)
		}
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
			horizontal = R.dimen.screen_horizontal_spacing,
			vertical = R.dimen.screen_vertical_spacing
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
		Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
private fun SortOptionsDialog(viewModel: AllEmployeesViewModel) {
	BasicAlertDialog(
		onDismissRequest = {
			viewModel.sortDialogDismissed()
		},
	) {
		Surface(
			modifier = Modifier.fillMaxWidth(),
			tonalElevation = 4.dp,
			shape = RoundedCornerShape(16.dp)
		) {
			Column(
				modifier = Modifier.padding(vertical = R.dimen.screen_vertical_spacing)
			) {
				Text(
					modifier = Modifier.padding(horizontal = 24.dp, bottom = 16.dp),
					style = MaterialTheme.typography.titleLarge,
					text = stringResource(R.string.sort_by)
				)
				Text(
					modifier = Modifier
						.clickable { viewModel.sortSelected(SortOption.NAME) }
						.padding(24.dp, 8.dp)
						.fillMaxWidth(),
					style = MaterialTheme.typography.bodyLarge,
					text = stringResource(R.string.name))
				Text(
					modifier = Modifier
						.clickable { viewModel.sortSelected(SortOption.TEAM) }
						.padding(24.dp, 8.dp)
						.fillMaxWidth(),
					style = MaterialTheme.typography.bodyLarge,
					text = stringResource(R.string.team))
			}
		}
	}
}

@Composable
private fun TopAppBar(viewModel: AllEmployeesViewModel) {
	TopAppBar(actions = {
		IconButton(
			onClick = {
				viewModel.sortMenuItemClicked()
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