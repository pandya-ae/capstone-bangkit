package com.dicoding.picodiploma.docplant.model

import com.google.gson.annotations.SerializedName

data class RegisterModel(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
