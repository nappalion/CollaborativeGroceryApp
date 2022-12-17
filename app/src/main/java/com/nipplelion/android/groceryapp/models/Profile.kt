package com.nipplelion.android.groceryapp.models

data class Profile(
    val username: String,
    val image: String = "",
    val accountCreated: Long,
    val email: String
)

