package com.dicoding.picodiploma.loginwithanimation.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryItem

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        @Suppress("DEPRECATION") val story = intent.getParcelableExtra<StoryItem>("story_item")

        story?.let {
            findViewById<TextView>(R.id.tv_detail_name).text = it.name
            findViewById<TextView>(R.id.tv_detail_description).text = it.description

            val locationText = if (it.lat != null && it.lon != null) {
                "Latitude: ${it.lat}, Longitude: ${it.lon}"
            } else {
                "Location not available"
            }
            findViewById<TextView>(R.id.tv_detail_location).text = locationText

            Glide.with(this)
                .load(it.photoUrl)
                .into(findViewById(R.id.iv_detail_photo))
        }
    }
}
