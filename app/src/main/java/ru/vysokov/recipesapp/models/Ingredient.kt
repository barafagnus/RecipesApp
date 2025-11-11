package ru.vysokov.recipesapp.models

import android.graphics.drawable.Drawable

data class Ingredient(
    val quantity: Double,
    val unitOfMeasure: String,
    val description: String
)