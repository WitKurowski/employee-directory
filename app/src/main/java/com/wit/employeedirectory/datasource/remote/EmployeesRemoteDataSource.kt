package com.wit.employeedirectory.datasource.remote

import com.google.gson.annotations.SerializedName
import com.wit.employeedirectory.model.Employee
import retrofit2.http.GET

interface EmployeesRemoteDataSource {
	@GET("employees.json")
	suspend fun getAllEmployees(): GetEmployeesResponse

	data class GetEmployeesResponse(@SerializedName("employees") val employees: List<Employee>)
}