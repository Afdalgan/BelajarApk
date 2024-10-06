package com.example.aplikasiafdal

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hero(
    val name: String,
    val description: String,
    val atribut: String,
    val photo: Int
) : Parcelable
