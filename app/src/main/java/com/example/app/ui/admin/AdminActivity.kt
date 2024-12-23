package com.example.app.ui.admin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.app.R
import com.example.app.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val statusBarColor = ContextCompat.getColor(this, R.color.statusBarColor)
        window.statusBarColor = statusBarColor  // Mengubah warna status bar

        // Inisialisasi NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        var navController = navHostFragment.navController

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)

        // Memeriksa role pengguna
        val role = sharedPreferences.getString("user_role", null)
        if (role == null || role != "admin") {
            // Jika tidak ada role atau role bukan "admin", arahkan ke login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation_admin)

        // Set listener for BottomNavigationView
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(AdminHomeFragment())
                    true
                }
                R.id.nav_report -> {
                    loadFragment(AdminReportFragment())
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(AdminProfileFragment())
                    true
                }
                else -> false
            }
        }

        // Set the default fragment when Activity is first loaded
        if (savedInstanceState == null) {
            loadFragment(AdminHomeFragment())
        }
    }

    // Function to load a fragment into the fragment container
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
}
