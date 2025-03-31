package com.wit.employeedirectory.di

import com.wit.employeedirectory.annotation.IODispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@InstallIn(SingletonComponent::class)
@Module
class CoroutineDispatcherModule {
	@IODispatcher
	@Provides
	fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}