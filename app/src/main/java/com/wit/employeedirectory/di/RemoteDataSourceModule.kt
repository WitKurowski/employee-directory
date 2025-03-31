package com.wit.employeedirectory.di

import com.wit.employeedirectory.datasource.remote.EmployeesRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@InstallIn(SingletonComponent::class)
@Module
object RemoteDataSourceModule {
	@Provides
	fun provideEmployeesRemoteDataSource(retrofit: Retrofit): EmployeesRemoteDataSource =
		retrofit.create(EmployeesRemoteDataSource::class.java)

	@Provides
	fun provideRetrofit(): Retrofit {
		val retrofit = Retrofit.Builder().run {
			baseUrl("https://s3.amazonaws.com/sq-mobile-interview/")
			addConverterFactory(GsonConverterFactory.create())
			build()
		}

		return retrofit
	}
}