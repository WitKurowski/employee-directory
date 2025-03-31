package com.wit.employeedirectory.feature.employee

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wit.employeedirectory.repository.EmployeesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AllEmployeesViewModel @Inject constructor(private val employeesRepository: EmployeesRepository) :
	ViewModel() {
	private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
		// TODO: Log unexpected throwable to something like Crashlytics.

		// TODO: Consider switching to using Timber.
		val message = "Caught expected throwable in ${this::class.simpleName}"
		Log.e(AllEmployeesViewModel::class.simpleName, message, throwable)

		_errorStateFlow.update {
			it.copy(visible = true)
		}
	}

	@VisibleForTesting
	val _employeeStatesFlow = MutableStateFlow<List<EmployeeState>>(emptyList())
	val employeeStatesFlow = _employeeStatesFlow.asStateFlow()
	private val _emptyStateFlow = MutableStateFlow(EmptyState(visible = false))
	val emptyStateFlow = _emptyStateFlow.asStateFlow()
	private val _errorStateFlow = MutableStateFlow(ErrorState(visible = false))
	val errorStateFlow = _errorStateFlow.asStateFlow()
	private val _loadingStateFlow = MutableStateFlow(LoadingState(visible = false))
	val loadingStateFlow = _loadingStateFlow.asStateFlow()

	fun fetchAllEmployees(forceRefresh: Boolean = false) {
		viewModelScope.launch(coroutineExceptionHandler) {
			_emptyStateFlow.update {
				it.copy(visible = false)
			}

			_errorStateFlow.update {
				it.copy(visible = false)
			}

			val fetchNeeded = forceRefresh || _employeeStatesFlow.value.isEmpty()

			_loadingStateFlow.update {
				it.copy(visible = fetchNeeded)
			}

			if (fetchNeeded) {
				try {
					val employees = employeesRepository.getAllEmployees()

					if (employees.isEmpty()) {
						_employeeStatesFlow.emit(emptyList())

						_emptyStateFlow.update {
							it.copy(visible = true)
						}
					} else {
						val employeeStates = employees.map {
							EmployeeState(it.id, it.name, it.photoUrlString, it.team)
						}

						_employeeStatesFlow.emit(employeeStates)
					}
				} catch (httpException: HttpException) {
					_errorStateFlow.update {
						it.copy(visible = true)
					}
				} catch (ioException: IOException) {
					_errorStateFlow.update {
						it.copy(visible = true)
					}
				} finally {
					_loadingStateFlow.update {
						it.copy(visible = false)
					}
				}
			}
		}
	}

	fun sortSelected(sortOption: SortOption) {
		viewModelScope.launch(coroutineExceptionHandler) {
			val employees = _employeeStatesFlow.value
			val sortedEmployees = when (sortOption) {
				SortOption.NAME -> employees.sortedBy { it.name }
				SortOption.TEAM -> {
					employees //
						.sortedBy { it.name } //
						.sortedBy { it.team }
				}
			}

			_employeeStatesFlow.emit(sortedEmployees)
		}
	}
}