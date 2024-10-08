package com.example.aplikasiafdal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Splash : AppCompatActivity() {
    companion object {
        private const val SPLASH_DISPLAY_LENGTH = 3000L // 3 detik
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.anim)
        findViewById<ImageView>(R.id.splash_logo).startAnimation(fadeIn)

        Handler(Looper.getMainLooper()).postDelayed({
            val mainIntent = Intent(this@Splash, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, SPLASH_DISPLAY_LENGTH)
    }
}