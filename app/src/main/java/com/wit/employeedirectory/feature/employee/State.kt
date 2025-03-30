package com.wit.employeedirectory.feature.employee

data class EmployeeState(
	val id: String, val name: String, val photoUrlString: String, val team: String
)

data class EmptyState(val visible: Boolean)

data class ErrorState(val visible: Boolean)

data class LoadingState(val visible: Boolean)