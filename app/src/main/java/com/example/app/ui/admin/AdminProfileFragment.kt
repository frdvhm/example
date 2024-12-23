package com.example.app.ui.admin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.app.R
import com.example.app.ui.login.LoginActivity

class AdminProfileFragment : Fragment(R.layout.fragment_admin_profile) {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_profile, container, false)

        // Inisialisasi SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)


        // Menangani klik logout
        val btnLogout = view.findViewById<TextView>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            // Menghapus peran pengguna dari SharedPreferences
            val editor = sharedPreferences.edit()
            editor.remove("user_role") // Hapus role pengguna
            editor.apply()

            // Arahkan ke halaman login
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // Menutup AdminActivity setelah logout
        }

        return view
    }
}
