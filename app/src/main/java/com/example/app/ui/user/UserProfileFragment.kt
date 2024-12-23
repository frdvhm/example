package com.example.app.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.app.R
import com.example.app.ui.login.LoginActivity

class UserProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        // Menangani klik tombol Edit Profil
        // Casting ke TextView
        val btnEditProfil: TextView = view.findViewById(R.id.btnEditProfil)
        btnEditProfil.setOnClickListener {
            // Navigasi ke EditProfileUserFragment
            findNavController().navigate(R.id.action_userProfileFragment_to_editProfileUserFragment)
        }

        val btnLogout: TextView = view.findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {
            // Intent untuk pindah ke LoginActivity
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()  // Menutup ProfileFragment dan kembali ke LoginActivity
        }
        return view
    }
}
