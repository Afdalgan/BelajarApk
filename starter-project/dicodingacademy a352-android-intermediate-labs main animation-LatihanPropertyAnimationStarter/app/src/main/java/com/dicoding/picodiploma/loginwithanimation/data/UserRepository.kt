package com.dicoding.picodiploma.loginwithanimation.data

import android.util.Log
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryItem
import com.dicoding.picodiploma.loginwithanimation.data.response.UploudResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import retrofit2.Response


class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    fun getStories(): Flow<Result<List<StoryItem>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getStories() // Hapus parameter token
            emit(Result.Success(response.listStory))
        } catch (e: Exception) {
            Log.e("API_ERROR", e.message ?: "Unknown error")
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
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

    suspend fun uploadImage(
        token: String,
        imageFile: File,
        description: String,
    ): Result<UploudResponse> {
        return try {
            val requestFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("photo", imageFile.name, requestFile)
            val descriptionBody = description.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = apiService.uploadImage(
                "Bearer $token",
                body,
                descriptionBody
            )
            Result.Success(response)
        } catch (e: Exception) {
            Log.e("UploadError", "An error occurred", e)
            Result.Error("An error occurred: ${e.localizedMessage}")
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

