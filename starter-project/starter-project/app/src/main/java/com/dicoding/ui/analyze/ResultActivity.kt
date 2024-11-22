
package com.dicoding.ui.analyze

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.db.AppDatabase
import com.dicoding.asclepius.entity.HistoryEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private val db: AppDatabase by lazy { AppDatabase.getDatabase(applicationContext) }

    companion object {
        const val EXTRA_RESULTS = "extra_results"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_CONFIDENCE = "extra_confidence"
        const val EXTRA_TIMESTAMP = "extra_timestamp"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayResults()
        binding.btnSave.setOnClickListener {
            savePrediction()
        }
    }

    private fun displayResults() {
        val results = intent.getStringExtra(EXTRA_RESULTS) ?: getString(R.string.no_results_available)
        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        val imageUri = imageUriString?.let { Uri.parse(it) }

        binding.resultImage.setImageURI(imageUri)
        binding.resultText.text = results
    }

    private fun savePrediction() {
        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        val result = intent.getStringExtra(EXTRA_RESULTS)
        val confidence = intent.getDoubleExtra(EXTRA_CONFIDENCE, 0.0)
        val timestamp = intent.getLongExtra(EXTRA_TIMESTAMP, System.currentTimeMillis())

        if (imageUriString == null || result == null) {
            Toast.makeText(this, "Error: Missing prediction data.", Toast.LENGTH_SHORT).show()
            return
        }

        val prediction = HistoryEntity(
            imageUri = imageUriString,
            result = result,
            confidence = confidence,
            timestamp = timestamp
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                db.predictionHistoryDao().insertPrediction(prediction)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ResultActivity, "Prediction saved!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ResultActivity, "Failed to save prediction.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}
