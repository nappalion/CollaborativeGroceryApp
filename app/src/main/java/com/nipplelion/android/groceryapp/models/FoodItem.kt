package com.nipplelion.android.groceryapp.models

data class FoodItem(
    val userId: String?,
    val date: Long,
    val upc: String,
    val category: String,
    val foodId: String,
    val image: String,
    val label: String,
)