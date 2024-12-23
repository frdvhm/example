package com.example.app.ui.user

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.example.app.data.db.DatabaseHelper
import com.example.app.databinding.DialogUploadReportBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UploadReportDialogFragment : DialogFragment() {

    private var _binding: DialogUploadReportBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null
    private var currentPhotoPath: String? = null

    companion object {
        const val CAMERA_PERMISSION_CODE = 1001
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            binding.imageView.setImageURI(imageUri)
        } else {
            Toast.makeText(requireContext(), "Failed to capture image.", Toast.LENGTH_SHORT).show()
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
            binding.imageView.setImageURI(imageUri)
        } else {
            Toast.makeText(requireContext(), "No image selected.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogUploadReportBinding.inflate(inflater, container, false)
        val root: View = binding.root

        checkPermissions()

        // Setup listeners
        binding.imageView.setOnClickListener {
            showImagePickerDialog()
        }

        binding.btnUpload.setOnClickListener {
            handleReportSubmission()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        return root
    }

    private fun checkPermissions() {
        val permissions = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        val permissionsNeeded = permissions.filter {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(requireActivity(), permissionsNeeded.toTypedArray(), CAMERA_PERMISSION_CODE)
        }
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Select Image")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
                2 -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        try {
            val photoFile: File = createImageFile()
            imageUri = FileProvider.getUriForFile(
                requireContext(),
                "com.example.app.fileprovider",
                photoFile
            )
            cameraLauncher.launch(imageUri)
        } catch (e: IOException) {
            Toast.makeText(requireContext(), "Error creating file for camera.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = requireContext().getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", ".jpg", storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun handleReportSubmission() {
        val description = binding.etDescription.text.toString().trim()
        val location = binding.etLocation.text.toString().trim()
        val contact = binding.etContact.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()

        if (description.isEmpty() || location.isEmpty() || contact.isEmpty() || address.isEmpty() || imageUri == null) {
            Toast.makeText(requireContext(), "Please fill in all fields and select an image!", Toast.LENGTH_SHORT).show()
            return
        }

        val databaseHelper = DatabaseHelper(requireContext())

        // Menggunakan metode insertReport dari DatabaseHelper
        val result = databaseHelper.insertReport(
            photo = imageUri.toString(),
            description = description,
            location = location,
            contact = contact,
            address = address,
            status = "Pending" // Status default
        )

        if (result != -1L) { // Hasil -1 menunjukkan kegagalan
            Toast.makeText(requireContext(), "Report successfully submitted!", Toast.LENGTH_SHORT).show()
            dismiss() // Tutup dialog setelah submit
        } else {
            Toast.makeText(requireContext(), "Failed to submit report.", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
