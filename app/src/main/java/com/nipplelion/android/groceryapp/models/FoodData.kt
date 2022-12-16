package com.nipplelion.android.groceryapp.models

data class FoodData(
    val hints: List<Hint>,
    val parsed: List<Any>,
    val text: String
)