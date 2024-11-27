package com.dicoding.picodiploma.loginwithanimation.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import retrofit2.http.POST

data class StoriesResponse(
	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("listStory")
	val listStory: List<StoryItem>
)

@Parcelize
data class StoryItem(
	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("lat")
	val lat: Double? = null,

	@field:SerializedName("lon")
	val lon: Double? = null
) : Parcelable
