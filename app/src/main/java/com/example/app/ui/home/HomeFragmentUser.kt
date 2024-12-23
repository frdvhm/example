package com.example.app.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.app.R

class HomeFragmentUser : Fragment(R.layout.fragment_user_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Menangani klik tombol Upload
        val btnUpload: Button = view.findViewById(R.id.btnAddLocation)
        btnUpload.setOnClickListener {
            // Menyembunyikan tombol setelah diklik
            btnUpload.visibility = View.GONE

            // Navigasi ke UserReportFragment menggunakan NavController
            findNavController().navigate(R.id.action_userHomeFragment_to_userReportFragment)
        }
    }
}
