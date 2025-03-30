package com.wit.employeedirectory.feature.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wit.employeedirectory.repository.EmployeesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllEmployeesViewModel @Inject constructor(private val employeesRepository: EmployeesRepository) :
	ViewModel() {
	private val _employeeStatesFlow = MutableStateFlow<List<EmployeeState>>(emptyList())
	val employeeStatesFlow = _employeeStatesFlow.asStateFlow()

	fun fetchAllEmployees() {
		viewModelScope.launch {
			val employees = employeesRepository.getAllEmployees()
			val employeeStates = employees.map {
				EmployeeState(it.id, it.name, it.photoUrlString)
			}

			_employeeStatesFlow.emit(employeeStates)
		}
	}
}