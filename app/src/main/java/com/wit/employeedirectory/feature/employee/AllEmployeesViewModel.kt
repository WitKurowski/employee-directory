package com.wit.employeedirectory.feature.employee

import androidx.lifecycle.ViewModel
import com.wit.employeedirectory.repository.EmployeesRepository
import javax.inject.Inject

class AllEmployeesViewModel @Inject constructor(private val employeesRepository: EmployeesRepository) :
	ViewModel()