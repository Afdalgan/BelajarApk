package com.example.aplikasiafdal

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView

class DescActivity : AppCompatActivity() {
    companion object {
        const val KEY_HERO = "key_hero"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desc)

        val dataHero = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(KEY_HERO, Hero::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(KEY_HERO)
        }

        if (dataHero != null) {
            val tvDetailName = findViewById<TextView>(R.id.tv_name)
            val tvDetailDescription = findViewById<TextView>(R.id.tv_description)
            val ivDetailPhoto = findViewById<ImageView>(R.id.iv_detail_photo)
            val tvPhysicalAttack = findViewById<TextView>(R.id.tv_physical_attack)

            tvDetailName.text = dataHero.name
            tvDetailDescription.text = dataHero.description
            ivDetailPhoto.setImageResource(dataHero.photo)
            tvPhysicalAttack.text = dataHero.atribut
        }
    }
}
