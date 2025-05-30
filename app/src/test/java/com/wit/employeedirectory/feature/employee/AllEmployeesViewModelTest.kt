package com.wit.employeedirectory.feature.employee

import com.wit.employeedirectory.image.ImageLoader
import com.wit.employeedirectory.model.Employee
import com.wit.employeedirectory.repository.EmployeesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

class AllEmployeesViewModelTest {
	@MockK
	lateinit var employeesRepository: EmployeesRepository

	@MockK
	lateinit var httpException: HttpException

	@MockK
	lateinit var imageLoader: ImageLoader

	@MockK
	lateinit var ioException: IOException

	private val testDispatcher = StandardTestDispatcher()
	private lateinit var viewModel: AllEmployeesViewModel

	@Before
	fun setup() {
		Dispatchers.setMain(testDispatcher)
		MockKAnnotations.init(this, relaxUnitFun = true)
		viewModel = AllEmployeesViewModel(employeesRepository, imageLoader)
	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun `show empty state when an empty list of employees is returned`() = runTest(testDispatcher) {
		coEvery { employeesRepository.getAllEmployees() } returns emptyList()

		val actualEmployeeStates = mutableListOf<List<EmployeeState>>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.employeeStatesFlow.toList(actualEmployeeStates)
		}
		val actualEmptyStates = mutableListOf<EmptyState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.emptyStateFlow.toList(actualEmptyStates)
		}
		val actualErrorStates = mutableListOf<ErrorState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.errorStateFlow.toList(actualErrorStates)
		}
		val actualLoadingStates = mutableListOf<LoadingState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.loadingStateFlow.toList(actualLoadingStates)
		}

		viewModel.fetchAllEmployees()
		testDispatcher.scheduler.advanceUntilIdle()

		coVerifySequence { employeesRepository.getAllEmployees() }

		assertEquals(listOf(emptyList<EmployeeState>()), actualEmployeeStates)
		assertEquals(
			listOf(EmptyState(visible = false), EmptyState(visible = true)), actualEmptyStates
		)
		assertEquals(listOf(ErrorState(visible = false)), actualErrorStates)
		assertEquals(
			listOf(
				LoadingState(visible = false),
				LoadingState(visible = true),
				LoadingState(visible = false)
			), actualLoadingStates
		)
	}

	@Test
	fun `show employees when a non-empty list of employees is returned`() = runTest(testDispatcher) {
		val id1 = "1"
		val name1 = "Bob"
		val photoUrlString1 = "http://photo.url.string/1.jpg"
		val team1 = "Team 1"
		val id2 = "2"
		val name2 = "Sue"
		val photoUrlString2 = "http://photo.url.string/2.jpg"
		val team2 = "Team 2"
		val id3 = "3"
		val name3 = "Anne"
		val photoUrlString3 = "http://photo.url.string/3.jpg"
		val team3 = "Team 3"
		val employees = listOf(
			Employee(id = id1, name = name1, photoUrlString = photoUrlString1, team = team1),
			Employee(id = id2, name = name2, photoUrlString = photoUrlString2, team = team2),
			Employee(id = id3, name = name3, photoUrlString = photoUrlString3, team = team3)
		)
		coEvery { employeesRepository.getAllEmployees() } returns employees

		val actualEmployeeStates = mutableListOf<List<EmployeeState>>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.employeeStatesFlow.toList(actualEmployeeStates)
		}
		val actualEmptyStates = mutableListOf<EmptyState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.emptyStateFlow.toList(actualEmptyStates)
		}
		val actualErrorStates = mutableListOf<ErrorState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.errorStateFlow.toList(actualErrorStates)
		}
		val actualLoadingStates = mutableListOf<LoadingState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.loadingStateFlow.toList(actualLoadingStates)
		}

		viewModel.fetchAllEmployees()
		testDispatcher.scheduler.advanceUntilIdle()

		coVerifySequence { employeesRepository.getAllEmployees() }

		val employeeState1 = EmployeeState(id1, name1, photoUrlString1, team1)
		val employeeState2 = EmployeeState(id2, name2, photoUrlString2, team2)
		val employeeState3 = EmployeeState(id3, name3, photoUrlString3, team3)
		val expectedEmployeeStates = listOf(
			employeeState1, employeeState2, employeeState3
		)
		assertEquals(listOf(emptyList(), expectedEmployeeStates), actualEmployeeStates)
		assertEquals(listOf(EmptyState(visible = false)), actualEmptyStates)
		assertEquals(listOf(ErrorState(visible = false)), actualErrorStates)
		assertEquals(
			listOf(
				LoadingState(visible = false),
				LoadingState(visible = true),
				LoadingState(visible = false)
			), actualLoadingStates
		)
	}

	@Test
	fun `replace employee list with newly fetched set of employees when refresh forced`() = runTest(testDispatcher) {
		val id1 = "1"
		val name1 = "Bob"
		val photoUrlString1 = "http://photo.url.string/1.jpg"
		val team1 = "Team 1"
		val id2 = "2"
		val name2 = "Sue"
		val photoUrlString2 = "http://photo.url.string/2.jpg"
		val team2 = "Team 2"
		val id3 = "3"
		val name3 = "Anne"
		val photoUrlString3 = "http://photo.url.string/3.jpg"
		val team3 = "Team 3"
		val employeeState1 = EmployeeState(id1, name1, photoUrlString1, team1)
		val employeeState2 = EmployeeState(id2, name2, photoUrlString2, team2)
		val employeeState3 = EmployeeState(id3, name3, photoUrlString3, team3)
		val initialEmployeeStates = listOf(employeeState3, employeeState2, employeeState1)
		viewModel._employeeStatesFlow.value = initialEmployeeStates

		val employees = listOf(
			Employee(id = id1, name = name1, photoUrlString = photoUrlString1, team = team1),
			Employee(id = id2, name = name2, photoUrlString = photoUrlString2, team = team2),
			Employee(id = id3, name = name3, photoUrlString = photoUrlString3, team = team3)
		)

		coEvery { employeesRepository.getAllEmployees() } returns employees

		val actualEmployeeStates = mutableListOf<List<EmployeeState>>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.employeeStatesFlow.toList(actualEmployeeStates)
		}
		val actualEmptyStates = mutableListOf<EmptyState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.emptyStateFlow.toList(actualEmptyStates)
		}
		val actualErrorStates = mutableListOf<ErrorState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.errorStateFlow.toList(actualErrorStates)
		}
		val actualLoadingStates = mutableListOf<LoadingState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.loadingStateFlow.toList(actualLoadingStates)
		}

		viewModel.fetchAllEmployees(forceRefresh = true)
		testDispatcher.scheduler.advanceUntilIdle()

		coVerifySequence { employeesRepository.getAllEmployees() }

		val finalEmployeeStates = listOf(
			employeeState1, employeeState2, employeeState3
		)
		assertEquals(listOf(initialEmployeeStates, finalEmployeeStates), actualEmployeeStates)
		assertEquals(listOf(EmptyState(visible = false)), actualEmptyStates)
		assertEquals(listOf(ErrorState(visible = false)), actualErrorStates)
		assertEquals(
			listOf(
				LoadingState(visible = false),
				LoadingState(visible = true),
				LoadingState(visible = false)
			), actualLoadingStates
		)
	}

	@Test
	fun `show error state when HttpException is thrown`() = runTest(testDispatcher) {
		coEvery { employeesRepository.getAllEmployees() } throws httpException

		val actualEmployeeStates = mutableListOf<List<EmployeeState>>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.employeeStatesFlow.toList(actualEmployeeStates)
		}
		val actualEmptyStates = mutableListOf<EmptyState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.emptyStateFlow.toList(actualEmptyStates)
		}
		val actualErrorStates = mutableListOf<ErrorState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.errorStateFlow.toList(actualErrorStates)
		}
		val actualLoadingStates = mutableListOf<LoadingState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.loadingStateFlow.toList(actualLoadingStates)
		}

		viewModel.fetchAllEmployees()
		testDispatcher.scheduler.advanceUntilIdle()

		coVerifySequence { employeesRepository.getAllEmployees() }

		assertEquals(listOf(emptyList<EmployeeState>()), actualEmployeeStates)
		assertEquals(listOf(EmptyState(visible = false)), actualEmptyStates)
		assertEquals(
			listOf(ErrorState(visible = false), ErrorState(visible = true)), actualErrorStates
		)
		assertEquals(
			listOf(
				LoadingState(visible = false),
				LoadingState(visible = true),
				LoadingState(visible = false)
			), actualLoadingStates
		)
	}

	@Test
	fun `show error state when IOException is thrown`() = runTest(testDispatcher) {
		coEvery { employeesRepository.getAllEmployees() } throws ioException

		val actualEmployeeStates = mutableListOf<List<EmployeeState>>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.employeeStatesFlow.toList(actualEmployeeStates)
		}
		val actualEmptyStates = mutableListOf<EmptyState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.emptyStateFlow.toList(actualEmptyStates)
		}
		val actualErrorStates = mutableListOf<ErrorState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.errorStateFlow.toList(actualErrorStates)
		}
		val actualLoadingStates = mutableListOf<LoadingState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.loadingStateFlow.toList(actualLoadingStates)
		}

		viewModel.fetchAllEmployees()
		testDispatcher.scheduler.advanceUntilIdle()

		coVerifySequence { employeesRepository.getAllEmployees() }

		assertEquals(listOf(emptyList<EmployeeState>()), actualEmployeeStates)
		assertEquals(listOf(EmptyState(visible = false)), actualEmptyStates)
		assertEquals(
			listOf(ErrorState(visible = false), ErrorState(visible = true)), actualErrorStates
		)
		assertEquals(
			listOf(
				LoadingState(visible = false),
				LoadingState(visible = true),
				LoadingState(visible = false)
			), actualLoadingStates
		)
	}

	@Test
	fun `sort dialog dismissed`() = runTest(testDispatcher) {
		val actualSortDialogVisibleStates = mutableListOf<Boolean>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.sortDialogVisibleStateFlow.toList(actualSortDialogVisibleStates)
		}

		viewModel.sortDialogDismissed()
		testDispatcher.scheduler.advanceUntilIdle()

		assertEquals(listOf(false), actualSortDialogVisibleStates)
	}

	@Test
	fun `sort menu item clicked`() = runTest(testDispatcher) {
		val actualSortDialogVisibleStates = mutableListOf<Boolean>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.sortDialogVisibleStateFlow.toList(actualSortDialogVisibleStates)
		}

		viewModel.sortMenuItemClicked()
		testDispatcher.scheduler.advanceUntilIdle()

		assertEquals(listOf(false, true), actualSortDialogVisibleStates)
	}

	@Test
	fun `sort empty employee list by name`() = runTest(testDispatcher) {
		val actualEmployeeStates = mutableListOf<List<EmployeeState>>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.employeeStatesFlow.toList(actualEmployeeStates)
		}
		val actualEmptyStates = mutableListOf<EmptyState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.emptyStateFlow.toList(actualEmptyStates)
		}
		val actualErrorStates = mutableListOf<ErrorState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.errorStateFlow.toList(actualErrorStates)
		}
		val actualLoadingStates = mutableListOf<LoadingState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.loadingStateFlow.toList(actualLoadingStates)
		}

		viewModel.sortSelected(SortOption.NAME)
		testDispatcher.scheduler.advanceUntilIdle()

		assertEquals(listOf(emptyList<EmployeeState>()), actualEmployeeStates)
		assertEquals(listOf(EmptyState(visible = false)), actualEmptyStates)
		assertEquals(listOf(ErrorState(visible = false)), actualErrorStates)
		assertEquals(listOf(LoadingState(visible = false)), actualLoadingStates)
	}

	@Test
	fun `sort non-empty employee list by name`() = runTest(testDispatcher) {
		val id1 = "1"
		val name1 = "Bob"
		val photoUrlString1 = "http://photo.url.string/1.jpg"
		val team1 = "Team 1"
		val id2 = "2"
		val name2 = "Sue"
		val photoUrlString2 = "http://photo.url.string/2.jpg"
		val team2 = "Team 2"
		val id3 = "3"
		val name3 = "Anne"
		val photoUrlString3 = "http://photo.url.string/3.jpg"
		val team3 = "Team 3"
		val employeeState1 = EmployeeState(id1, name1, photoUrlString1, team1)
		val employeeState2 = EmployeeState(id2, name2, photoUrlString2, team2)
		val employeeState3 = EmployeeState(id3, name3, photoUrlString3, team3)
		val initialEmployeeStates = listOf(employeeState1, employeeState2, employeeState3)
		viewModel._employeeStatesFlow.value = initialEmployeeStates

		val actualEmployeeStates = mutableListOf<List<EmployeeState>>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.employeeStatesFlow.toList(actualEmployeeStates)
		}
		val actualEmptyStates = mutableListOf<EmptyState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.emptyStateFlow.toList(actualEmptyStates)
		}
		val actualErrorStates = mutableListOf<ErrorState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.errorStateFlow.toList(actualErrorStates)
		}
		val actualLoadingStates = mutableListOf<LoadingState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.loadingStateFlow.toList(actualLoadingStates)
		}

		viewModel.sortSelected(SortOption.NAME)
		testDispatcher.scheduler.advanceUntilIdle()

		val finalEmployeeStates = listOf(employeeState3, employeeState1, employeeState2)
		assertEquals(listOf(initialEmployeeStates, finalEmployeeStates), actualEmployeeStates)
		assertEquals(listOf(EmptyState(visible = false)), actualEmptyStates)
		assertEquals(listOf(ErrorState(visible = false)), actualErrorStates)
		assertEquals(listOf(LoadingState(visible = false)), actualLoadingStates)
	}

	@Test
	fun `sort non-empty employee list by team`() = runTest(testDispatcher) {
		val id1 = "1"
		val name1 = "Bob"
		val photoUrlString1 = "http://photo.url.string/1.jpg"
		val team1 = "Team 3"
		val id2 = "2"
		val name2 = "Sue"
		val photoUrlString2 = "http://photo.url.string/2.jpg"
		val team2 = "Team 1"
		val id3 = "3"
		val name3 = "Anne"
		val photoUrlString3 = "http://photo.url.string/3.jpg"
		val team3 = "Team 2"
		val employeeState1 = EmployeeState(id1, name1, photoUrlString1, team1)
		val employeeState2 = EmployeeState(id2, name2, photoUrlString2, team2)
		val employeeState3 = EmployeeState(id3, name3, photoUrlString3, team3)
		val initialEmployeeStates = listOf(employeeState1, employeeState2, employeeState3)
		viewModel._employeeStatesFlow.value = initialEmployeeStates

		val actualEmployeeStates = mutableListOf<List<EmployeeState>>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.employeeStatesFlow.toList(actualEmployeeStates)
		}
		val actualEmptyStates = mutableListOf<EmptyState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.emptyStateFlow.toList(actualEmptyStates)
		}
		val actualErrorStates = mutableListOf<ErrorState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.errorStateFlow.toList(actualErrorStates)
		}
		val actualLoadingStates = mutableListOf<LoadingState>()
		backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
			viewModel.loadingStateFlow.toList(actualLoadingStates)
		}

		viewModel.sortSelected(SortOption.TEAM)
		testDispatcher.scheduler.advanceUntilIdle()

		val finalEmployeeStates = listOf(employeeState2, employeeState3, employeeState1)
		assertEquals(listOf(initialEmployeeStates, finalEmployeeStates), actualEmployeeStates)
		assertEquals(listOf(EmptyState(visible = false)), actualEmptyStates)
		assertEquals(listOf(ErrorState(visible = false)), actualErrorStates)
		assertEquals(listOf(LoadingState(visible = false)), actualLoadingStates)
	}
}