package com.example.finalproject_cthru.data.remote.response

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Data(

	@field:SerializedName("datetime")
	val datetime: String? = null,

	@field:SerializedName("cataract_confidence")
	val cataractConfidence: Any? = null,

	@field:SerializedName("eye_confidence")
	val eyeConfidence: Any? = null,

	@field:SerializedName("cataract_detection")
	val cataractDetection: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("eye_detection")
	val eyeDetection: String? = null
)
