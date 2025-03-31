package com.wit.employeedirectory.repository

import com.wit.employeedirectory.annotation.IODispatcher
import com.wit.employeedirectory.datasource.remote.EmployeesRemoteDataSource
import com.wit.employeedirectory.model.Employee
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import kotlin.jvm.Throws

class EmployeesRepository @Inject constructor(
	private val employeesRemoteDataSource: EmployeesRemoteDataSource,
	@IODispatcher private val ioCoroutineDispatcher: CoroutineDispatcher
) {
	@Throws(IOException::class)
	suspend fun getAllEmployees(): List<Employee> {
		val employees = withContext(ioCoroutineDispatcher) {
			val employeesResponse = employeesRemoteDataSource.getAllEmployees()

			employeesResponse.employees
		}

		return employees
	}
}