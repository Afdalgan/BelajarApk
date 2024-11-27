package com.dicoding.picodiploma.loginwithanimation.view.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.response.UploudResponse
import java.io.File
import kotlinx.coroutines.launch
import com.dicoding.picodiploma.loginwithanimation.data.Result
class AddViewModel(private val repository: UserRepository) : ViewModel() {

    private val _uploadStatus = MutableLiveData<Result<UploudResponse>>()
    val uploadStatus: LiveData<Result<UploudResponse>> = _uploadStatus

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun uploadImage(token: String, imageFile: File, description: String) {
        viewModelScope.launch {
            _uploadStatus.postValue(Result.Loading)
            try {
                val result = repository.uploadImage(token, imageFile, description)
                _uploadStatus.postValue(result)
            } catch (e: Exception) {
                _uploadStatus.postValue(Result.Error("Upload failed: ${e.localizedMessage}"))
            }
        }
    }

}
