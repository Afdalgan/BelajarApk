package com.dicoding.picodiploma.loginwithanimation.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.StoriesResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryItem
import com.dicoding.picodiploma.loginwithanimation.data.response.UploadResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService
import com.dicoding.picodiploma.loginwithanimation.view.story.StoriesPagingSource
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class UserRepository(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    fun getStoriesPaging(): Flow<PagingData<StoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                StoriesPagingSource(apiService)
            }
        ).flow
    }

    suspend fun registerUser(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun loginUser(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun getStoriesWithLocation(): StoriesResponse {
        return apiService.getStoriesWithLocation()
    }


    suspend fun uploadImage(
        imageFile: File,
        description: String,
        lat: Double? = null,
        lon: Double? = null
    ): Result<UploadResponse> {
        return try {
            val requestFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("photo", imageFile.name, requestFile)
            val descriptionBody = description.toRequestBody("text/plain".toMediaTypeOrNull())

            val latBody = lat?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
            val lonBody = lon?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = apiService.uploadImageWithLocation(
                body,
                descriptionBody,
                latBody,
                lonBody
            )
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error("Upload failed: ${e.localizedMessage}")
        }
    }



    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(userPreference: UserPreference, apiService: ApiService): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}

