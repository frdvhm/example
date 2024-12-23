package com.example.app.data.db

data class UserProfile(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val dob: String,
    val location: String
)