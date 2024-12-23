package com.example.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.app.R
import com.example.app.data.db.AdminProfileDatabaseHelper
import com.example.app.data.db.AdminProfile

class EditProfileAdminFragment : Fragment() {

    private lateinit var dbHelper: AdminProfileDatabaseHelper
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private var adminId: Int = 0 // Simpan ID admin yang sedang login

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_edit_profile, container, false)

        dbHelper = AdminProfileDatabaseHelper(requireContext())
        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)

        // Mengambil adminId dari argument atau data yang disimpan sebelumnya
        adminId = arguments?.getInt("ADMIN_ID") ?: 0
        if (adminId != 0) {
            loadAdminProfile()
        } else {
            Toast.makeText(requireContext(), "Invalid Admin ID", Toast.LENGTH_SHORT).show()
        }

        // Tombol untuk mengupdate profil
        view.findViewById<View>(R.id.btnSave).setOnClickListener {
            updateAdminProfile()
        }

        return view
    }

    private fun loadAdminProfile() {
        val admin = dbHelper.getAdminProfile(adminId) // Menggunakan helper untuk mendapatkan data admin
        admin?.let {
            etName.setText(it.name)
            etEmail.setText(it.email)
            etPassword.setText(it.password)
        } ?: run {
            Toast.makeText(requireContext(), "Admin profile not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateAdminProfile() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Validasi input
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Update profil admin
        val updated = dbHelper.updateAdminProfile(adminId, name, email, password)
        if (updated > 0) {
            Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close() // Menutup database dengan aman
    }
}
