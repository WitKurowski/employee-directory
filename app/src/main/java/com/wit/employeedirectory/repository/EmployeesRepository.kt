package com.wit.employeedirectory.repository

import com.wit.employeedirectory.datasource.remote.EmployeesRemoteDataSource
import com.wit.employeedirectory.model.Employee
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmployeesRepository @Inject constructor(private val employeesRemoteDataSource: EmployeesRemoteDataSource) {
	suspend fun getAllEmployees(): List<Employee> {
		// TODO: Inject dispatcher
		val employees = withContext(Dispatchers.IO) {
			val employeesResponse = employeesRemoteDataSource.getAllEmployees()

			employeesResponse.employees
		}

		return employees
	}
}