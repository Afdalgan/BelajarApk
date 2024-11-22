package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.BitmapFactory
import org.tensorflow.lite.support.image.TensorImage
import android.net.Uri
import android.util.Log
import com.dicoding.asclepius.R
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.lang.Exception

class ImageClassifierHelper(
    var threshold: Float = 0.1f,
    var maxResults: Int = 3,
    val modelName: String = "cancer_classification.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: List<Classifications>?,
            inferenceTime: Long
        )
    }

    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setMaxResults(maxResults)
            .setScoreThreshold(threshold)

        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val tensorImage = TensorImage.fromBitmap(bitmap)

            val startTime = System.nanoTime()

            val results = imageClassifier?.classify(tensorImage)

            val endTime = System.nanoTime()
            val inferenceTime = (endTime - startTime) / 1_000_000 // Dalam milidetik
            classifierListener?.onResults(results, inferenceTime)

        } catch (e: Exception) {
            // Menangani kesalahan saat klasifikasi gambar
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, "Error classifying image: ${e.message}")
        }
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}
