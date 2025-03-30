package com.wit.employeedirectory.feature.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wit.employeedirectory.repository.EmployeesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
	private val _employeeStatesFlow = MutableStateFlow<List<EmployeeState>>(emptyList())
	val employeeStatesFlow = _employeeStatesFlow.asStateFlow()
	private val _errorStateFlow = MutableStateFlow(ErrorState(visible = false))
	val errorStateFlow = _errorStateFlow.asStateFlow()

	fun fetchAllEmployees() {
		viewModelScope.launch {
			_errorStateFlow.update {
				it.copy(visible = false)
			}

			try {
				val employees = employeesRepository.getAllEmployees()
				val employeeStates = employees.map {
					EmployeeState(it.id, it.name, it.photoUrlString, it.team)
				}

				_employeeStatesFlow.emit(employeeStates)
			} catch (httpException: HttpException) {
				_errorStateFlow.update {
					it.copy(visible = true)
				}
			} catch (ioException: IOException) {
				_errorStateFlow.update {
					it.copy(visible = true)
				}
			}
		}
	}
}