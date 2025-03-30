package com.wit.employeedirectory.feature.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wit.employeedirectory.repository.EmployeesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllEmployeesViewModel @Inject constructor(private val employeesRepository: EmployeesRepository) :
	ViewModel() {
	fun fetchAllEmployees() {
		viewModelScope.launch {
			employeesRepository.getAllEmployees()
		}
	}
}