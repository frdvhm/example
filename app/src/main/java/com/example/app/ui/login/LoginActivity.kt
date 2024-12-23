package com.example.app.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app.R
import com.example.app.ui.admin.AdminActivity
import com.example.app.ui.user.UserActivity
import com.example.app.utils.RoleUtils

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var tvUser: TextView
    private lateinit var tvAdmin: TextView
    private lateinit var selectedRole: String // This will hold the role selection

    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inisialisasi komponen
        emailInput = findViewById(R.id.etEmail)
        passwordInput = findViewById(R.id.etPassword)
        loginButton = findViewById(R.id.btnLogin)
        tvUser = findViewById(R.id.tvUser)
        tvAdmin = findViewById(R.id.tvAdmin)

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)

        // Set listeners untuk memilih role User/Admin
        tvUser.setOnClickListener {
            selectedRole = "user"
            // Set default username and password for user
            emailInput.setText("user")
            passwordInput.setText("user")

            tvUser.setTextColor(resources.getColor(R.color.selected_role))  // Warna untuk yang dipilih
            tvAdmin.setTextColor(resources.getColor(R.color.unselected_role))  // Warna untuk yang tidak dipilih
        }

        tvAdmin.setOnClickListener {
            selectedRole = "admin"
            // Set default username and password for admin
            emailInput.setText("admin")
            passwordInput.setText("admin")

            tvAdmin.setTextColor(resources.getColor(R.color.selected_role))  // Warna untuk yang dipilih
            tvUser.setTextColor(resources.getColor(R.color.unselected_role))  // Warna untuk yang tidak dipilih
        }

        // Set listener untuk login button
        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && ::selectedRole.isInitialized) {
                // Panggil metode untuk memeriksa role berdasarkan email dan password
                val role = RoleUtils.getUserRole(email, password, selectedRole)
                if (role != null) {
                    // Simpan role di SharedPreferences
                    val editor = sharedPreferences.edit()
                    editor.putString("user_role", role)
                    editor.apply()

                    // Navigasi ke activity berdasarkan role
                    onLoginSuccess(role)
                } else {
                    Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Pilih Sebagai User atau Admin!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onLoginSuccess(role: String) {
        if (role == "admin") {
            startActivity(Intent(this, AdminActivity::class.java))
        } else if (role == "user") {
            startActivity(Intent(this, UserActivity::class.java))
        }
        finish()
    }
}
