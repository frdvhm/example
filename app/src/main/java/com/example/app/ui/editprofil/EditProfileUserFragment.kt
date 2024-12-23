package com.example.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.app.R
import com.example.app.data.db.UserProfileDatabaseHelper
import com.example.app.data.db.UserProfile

class EditProfileUserFragment : Fragment() {

    private lateinit var dbHelper: UserProfileDatabaseHelper
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etDob: EditText
    private lateinit var etLocation: EditText
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_edit_profile, container, false)

        // Ambil ID pengguna
        userId = arguments?.getInt("USER_ID") ?: 0
        if (userId == 0) {
            Toast.makeText(requireContext(), "Invalid User ID", Toast.LENGTH_SHORT).show()
            return view
        }

        // Inisialisasi komponen UI
        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        etDob = view.findViewById(R.id.etDob)
        etLocation = view.findViewById(R.id.etLocation)

        dbHelper = UserProfileDatabaseHelper(requireContext())
        loadUserProfile()

        // Tombol Save Changes
        view.findViewById<View>(R.id.btnSave).setOnClickListener {
            saveProfileChanges()
        }

        return view
    }

    private fun loadUserProfile() {
        val user = dbHelper.getUserProfile(userId)
        user?.let {
            etName.setText(it.name)
            etEmail.setText(it.email)
            etPassword.setText(it.password)
            etDob.setText(it.dob)
            etLocation.setText(it.location)
        } ?: run {
            Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveProfileChanges() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val dob = etDob.text.toString().trim()
        val location = etLocation.text.toString().trim()

        // Validasi input
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || dob.isEmpty() || location.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Simpan ke database
        val updated = dbHelper.updateUserProfile(userId, name, email, password, dob, location)
        if (updated > 0) {
            Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()

            // Navigasi kembali ke nav_profile
            val navController = androidx.navigation.Navigation.findNavController(requireView())
            navController.navigate(R.id.nav_profile)
        } else {
            Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}
