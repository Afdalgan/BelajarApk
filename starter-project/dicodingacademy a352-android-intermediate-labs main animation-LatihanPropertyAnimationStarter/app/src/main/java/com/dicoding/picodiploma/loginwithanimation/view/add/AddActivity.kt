package com.dicoding.picodiploma.loginwithanimation.view.add

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.Result
import com.dicoding.picodiploma.loginwithanimation.data.response.UploadResponse
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.*

class AddActivity : AppCompatActivity() {

    private lateinit var addViewModel: AddViewModel
    private lateinit var previewImageView: ImageView
    private lateinit var editTextDescription: EditText
    private lateinit var progressIndicator: ProgressBar
    private lateinit var checkBoxLocation: CheckBox
    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private var currentImageUri: Uri? = null
    private var previousImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        initViews()
        setupViewModel()
        setupListeners()
        observeViewModel()

        checkBoxLocation = findViewById(R.id.checkbox_add_location)
        checkBoxLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                fetchCurrentLocation()
            } else {
                currentLatitude = null
                currentLongitude = null
            }
        }

        if (savedInstanceState != null) {
            val savedUri = savedInstanceState.getString(IMAGE_URI_KEY)
            savedUri?.let {
                currentImageUri = Uri.parse(it)
                setImageWithPlaceholder(currentImageUri!!)
            }
        } else {
            previewImageView.setImageResource(R.drawable.placeholder)
        }

        val addToolbar: Toolbar = findViewById(R.id.addToolbar)
        setSupportActionBar(addToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        currentImageUri?.let {
            outState.putString(IMAGE_URI_KEY, it.toString())
        }
    }

    private fun initViews() {
        previewImageView = findViewById(R.id.previewImage)
        editTextDescription = findViewById(R.id.ed_add_description)
        progressIndicator = findViewById(R.id.progressIndicator)
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        addViewModel = ViewModelProvider(this, factory)[AddViewModel::class.java]
    }

    private fun setupListeners() {
        findViewById<Button>(R.id.btn_gallery).setOnClickListener { startGallery() }
        findViewById<Button>(R.id.btn_camera).setOnClickListener { checkCameraPermission() }
        findViewById<Button>(R.id.button_add).setOnClickListener { validateAndUpload() }
    }

    private fun observeViewModel() {
        addViewModel.uploadStatus.observe(this) { result ->
            handleUploadStatus(result)
        }
    }

    private fun fetchCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    currentLatitude = location.latitude
                    currentLongitude = location.longitude
                    showToast("Location fetched: $currentLatitude, $currentLongitude")
                } else {
                    showToast("Failed to fetch location. No location data available.")
                }
            }.addOnFailureListener {
                showToast("Failed to fetch location: ${it.message}")
            }
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                showToast("Location permission is needed to fetch your current location.")
            }
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchCurrentLocation()
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showToast("Location permission denied permanently. Enable it from app settings.")
                } else {
                    showToast("Permission denied. Cannot fetch location.")
                }
            }
        }
    }

    private fun validateAndUpload() {
        Log.d("AddActivity", "Current URI: $currentImageUri")
        val description = editTextDescription.text.toString()
        if (description.isBlank()) {
            showToast("Description is required")
            return
        }

        currentImageUri?.let { uri ->
            val file = uriToFile(uri, this)
            if (file.exists()) {
                val compressedFile = compressImage(file)
                showLoading(true)
                addViewModel.uploadImage(compressedFile, description, currentLatitude, currentLongitude)
            } else {
                showToast("Image file not found")
            }
        } ?: showToast("Please select an image first")
    }

    private fun handleUploadStatus(result: Result<UploadResponse>) {
        when (result) {
            is Result.Loading -> showLoading(true)
            is Result.Success -> {
                showLoading(false)
                showToast(result.data.message ?: "Story created successfully")
                navigateToMainActivity()
            }
            is Result.Error -> {
                showLoading(false)
                showToast(result.error)
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
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
        previousImageUri = currentImageUri
        currentImageUri = getImageUri(this)
        currentImageUri?.let { launcherIntentCamera.launch(it) }
    }

    private fun getImageUri(context: Context): Uri? {
        val timeStamp = System.currentTimeMillis().toString()
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_$timeStamp.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let {
                currentImageUri = it
                setImageWithPlaceholder(it)
            }
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            currentImageUri?.let { uri ->
                setImageWithPlaceholder(uri)
            }
        } else {
            currentImageUri = previousImageUri
            showToast("Photo capture canceled")
        }
    }

    private fun setImageWithPlaceholder(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 200, 200, false)
            previewImageView.setImageBitmap(resizedBitmap)
            inputStream?.close()
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("Failed to load image")
        }
    }

    private fun compressImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 1024, 1024, true)
        val compressQuality = 80
        val outputStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, outputStream)
        val compressedFile = File.createTempFile("compressed_image", ".jpg", file.parentFile)
        FileOutputStream(compressedFile).use {
            it.write(outputStream.toByteArray())
        }
        return compressedFile
    }

    private fun uriToFile(imageUri: Uri, context: Context): File {
        val file = File.createTempFile("temp_image", ".jpg", context.cacheDir)
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
        private const val IMAGE_URI_KEY = "image_uri"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1002

    }
}
