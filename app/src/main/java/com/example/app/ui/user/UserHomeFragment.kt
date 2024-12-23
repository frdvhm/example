package com.example.app.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.app.R
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style

class UserHomeFragment : Fragment() {

    private var _mapView: MapView? = null
    private val mapView get() = _mapView!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout fragment_user_home
        return inflater.inflate(R.layout.fragment_user_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi MapView
        _mapView = view.findViewById(R.id.mapView)

        // Mengatur style dan posisi kamera menggunakan mapboxMap
        mapView.getMapboxMap().apply {
            loadStyleUri(Style.MAPBOX_STREETS) // Mengatur style peta
            setCamera(
                CameraOptions.Builder()
                    .center(Point.fromLngLat(110.4107245, -7.6867115)) // Koordinat
                    .zoom(16.0) // Zoom level
                    .build()
            )
        }

        // Button untuk membuka dialog
        val btnAddLocation: Button = view.findViewById(R.id.btnAddLocation)
        btnAddLocation.setOnClickListener {
            // Tampilkan dialog
            val dialog = UploadReportDialogFragment()
            dialog.show(parentFragmentManager, "UploadReportDialog")
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart() // Mulai MapView
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop() // Hentikan MapView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy() // Hancurkan MapView
        _mapView = null // Bersihkan referensi untuk mencegah kebocoran memori
    }
}
