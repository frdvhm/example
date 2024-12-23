package com.example.app.utils

object RoleUtils {
    fun getUserRole(email: String, password: String, role: String): String? {
        // Implementasi untuk memeriksa email, password dan role (user/admin)
        // Logika ini bisa disesuaikan dengan backend atau database Anda
        if (role == "user" && email == "user" && password == "user") {
            return "user"
        } else if (role == "admin" && email == "admin" && password == "admin") {
            return "admin"
        }
        return null
    }
}
