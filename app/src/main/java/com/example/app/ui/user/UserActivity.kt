package com.example.app.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.app.R
import com.example.app.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        // Mengubah warna status bar
        val statusBarColor = ContextCompat.getColor(this, R.color.statusBarColor)
        window.statusBarColor = statusBarColor

        // Inisialisasi NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Memeriksa role pengguna
        val role = getSharedPreferences("app_prefs", MODE_PRIVATE).getString("user_role", null)
        if (role == null || role != "user") {
            // Jika role bukan "user", arahkan ke login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Initialize BottomNavigationView
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation_user)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.userHomeFragment)
                    true
                }
                R.id.nav_report -> {
                    navController.navigate(R.id.userReportFragment)
                    true
                }
                R.id.nav_profile -> {
                    navController.navigate(R.id.userProfileFragment)
                    true
                }
                else -> false
            }
        }

    }
}
