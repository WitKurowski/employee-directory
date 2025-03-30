package com.wit.employeedirectory.repository

import com.wit.employeedirectory.datasource.remote.EmployeesRemoteDataSource
import javax.inject.Inject

class EmployeesRepository @Inject constructor(private val employeesRemoteDataSource: EmployeesRemoteDataSource)