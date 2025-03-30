package com.wit.employeedirectory.model

import com.google.gson.annotations.SerializedName

data class Employee(@SerializedName("uuid") val id: String)