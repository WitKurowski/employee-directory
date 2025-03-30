package com.wit.employeedirectory.di

import com.wit.employeedirectory.image.GlideImageLoader
import com.wit.employeedirectory.image.ImageLoader
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class ImageLoaderModule {
	@Binds
	abstract fun bindImageLoader(glideImageLoader: GlideImageLoader): ImageLoader
}