package com.wit.employeedirectory.annotation

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
@Target(
	AnnotationTarget.FIELD,
	AnnotationTarget.FUNCTION,
	AnnotationTarget.TYPE,
	AnnotationTarget.VALUE_PARAMETER
)
annotation class IODispatcher
