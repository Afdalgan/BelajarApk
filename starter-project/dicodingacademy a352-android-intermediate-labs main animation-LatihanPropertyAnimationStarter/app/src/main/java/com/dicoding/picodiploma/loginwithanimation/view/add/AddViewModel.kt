package com.dicoding.picodiploma.loginwithanimation.view.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.response.UploadResponse
import java.io.File
import kotlinx.coroutines.launch
import com.dicoding.picodiploma.loginwithanimation.data.Result
class AddViewModel(private val repository: UserRepository) : ViewModel() {

    private val _uploadStatus = MutableLiveData<Result<UploadResponse>>()
    val uploadStatus: LiveData<Result<UploadResponse>> = _uploadStatus

    fun uploadImage(imageFile: File, description: String, lat: Double?, lon: Double?) {
        viewModelScope.launch {
            _uploadStatus.postValue(Result.Loading)
            try {
                val result = repository.uploadImage(imageFile, description, lat, lon)
                _uploadStatus.postValue(result)
            } catch (e: Exception) {
                _uploadStatus.postValue(Result.Error("Upload failed: ${e.localizedMessage}"))
            }
        }
    }
}

