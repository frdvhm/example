package com.example.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.app.R
import com.example.app.ui.login.LoginActivity

class ProfileFragmentUser : Fragment(R.layout.fragment_user_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Menangani klik tombol Edit Profil
        val btnEditProfil: TextView = view.findViewById(R.id.btnEditProfil)
        btnEditProfil.setOnClickListener {
            // Menavigasi ke EditProfileUserFragment menggunakan NavController
            findNavController().navigate(R.id.action_userProfileFragment_to_editProfileUserFragment)
        }

        // Menangani klik tombol Logout
        val btnLogout: TextView = view.findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {
            // Intent untuk pindah ke LoginActivity
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // Menutup ProfileFragment dan kembali ke LoginActivity
        }
    }
}
