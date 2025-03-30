package com.wit.employeedirectory.model

import com.google.gson.annotations.SerializedName

data class Employee(
	@SerializedName("uuid") val id: String,
	@SerializedName("full_name") val name: String,
	@SerializedName("photo_url_small") val photoUrlString: String,
	@SerializedName("team") val team: String
)