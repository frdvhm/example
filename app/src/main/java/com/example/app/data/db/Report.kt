package com.example.app.data.db

data class Report(
    val id: Int,
    val photo: String,
    val description: String,
    val location: String,
    val contact: String,
    val address: String,
    var status: String
)



