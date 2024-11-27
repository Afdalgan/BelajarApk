package com.dicoding.picodiploma.loginwithanimation.view.add

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.loginwithanimation.BuildConfig
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import com.dicoding.picodiploma.loginwithanimation.data.Result
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.pref.dataStore
import com.dicoding.picodiploma.loginwithanimation.data.response.UploudResponse
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class AddActivity : AppCompatActivity() {

    private lateinit var addViewModel: AddViewModel
    private lateinit var previewImageView: ImageView
    private lateinit var editTextDescription: EditText
    private lateinit var progressIndicator: ProgressBar
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        initViews()
        setupViewModel()
        setupListeners()
        observeViewModel()
    }

    private fun initViews() {
        previewImageView = findViewById(R.id.previewImage)
        editTextDescription = findViewById(R.id.editText_description)
        progressIndicator = findViewById(R.id.progressIndicator)
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        addViewModel = ViewModelProvider(this, factory).get(AddViewModel::class.java)
    }

    private fun setupListeners() {
        findViewById<Button>(R.id.btn_gallery).setOnClickListener { startGallery() }
        findViewById<Button>(R.id.btn_camera).setOnClickListener { checkCameraPermission() }
        findViewById<Button>(R.id.btn_upload).setOnClickListener { validateAndUpload() }
    }

    private fun observeViewModel() {
        addViewModel.uploadStatus.observe(this) { result ->
            handleUploadStatus(result)
        }
    }

    private fun compressImage(file: File): File {
        return file.reduceFileImage()
    }

    private fun File.reduceFileImage(): File {
        val bitmap = BitmapFactory.decodeFile(this.path)
        var compressQuality = 100
        var streamLength: Int
        val MAXIMAL_SIZE = 1 * 1024 * 1024

        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            streamLength = bmpStream.toByteArray().size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE && compressQuality > 5)

        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(this))
        return this
    }



    private fun validateAndUpload() {
        val description = editTextDescription.text.toString()
        if (description.isBlank()) {
            showToast("Description is required")
            return
        }

        val userPreference = UserPreference.getInstance(applicationContext.dataStore)
        lifecycleScope.launch {
            userPreference.getSession().collect { userModel ->
                val token = userModel.token

                if (token.isNotBlank()) {
                    currentImageUri?.let { uri ->
                        val imageFile = uriToFile(uri, this@AddActivity)
                        val compressedImageFile = compressImage(imageFile)
                        showLoading(true)
                        addViewModel.uploadImage(token, compressedImageFile, description)
                    } ?: showToast("Please select an image first")
                } else {
                    showToast("User is not logged in or token is missing")
                }
            }
        }
    }


    private fun handleUploadStatus(result: Result<UploudResponse>) {
        showLoading(false)
        when (result) {
            is Result.Success -> showToast(result.data.message ?: "Upload successful")
            is Result.Error -> showToast(result.error)
            is Result.Loading -> showLoading(true)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
        launcherGallery.launch(intent)
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        currentImageUri?.let { launcherIntentCamera.launch(it) }
    }

    private fun getImageUri(context: Context): Uri? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_$timeStamp.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let {
                currentImageUri = it
                previewImageView.setImageURI(it)
            }
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) currentImageUri?.let { previewImageView.setImageURI(it) }
    }

    private fun uriToFile(imageUri: Uri, context: Context): File {
        val myFile = createCustomTempFile(context)
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
        outputStream.close()
        inputStream.close()
        return myFile
    }

    private fun createCustomTempFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis())
        val storageDir: File? = context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
    }
}
