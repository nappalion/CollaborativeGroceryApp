package com.nipplelion.android.groceryapp

class FoodModel() {

    companion object {
        lateinit var foods: List<Triple<String, String, String>>

        fun addFoodList(foodLabel: String, foodImage: String, foodUPC: String) {
            var foodToAdd = Triple(foodLabel, foodImage, foodUPC)
            //foods.(foodToAdd)
        }
    }
}