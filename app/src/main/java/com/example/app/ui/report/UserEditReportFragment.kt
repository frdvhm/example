package com.example.app.ui.report

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.app.R
import com.example.app.data.db.DatabaseHelper
import com.example.app.databinding.FragmentUserEditReportBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UserEditReportFragment : Fragment() {

    private var _binding: FragmentUserEditReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DatabaseHelper
    private var reportId: Int = -1
    private var imageUri: Uri? = null
    private var currentPhotoPath: String? = null

    companion object {
        const val CAMERA_PERMISSION_CODE = 1001
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            binding.ivReportPhoto.setImageURI(imageUri)
        } else {
            Toast.makeText(requireContext(), "Failed to capture image.", Toast.LENGTH_SHORT).show()
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
            binding.ivReportPhoto.setImageURI(imageUri)
        } else {
            Toast.makeText(requireContext(), "No image selected.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserEditReportBinding.inflate(inflater, container, false)
        dbHelper = DatabaseHelper(requireContext())
        reportId = arguments?.getInt("REPORT_ID") ?: -1
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadReportData()
        setupListeners()
    }

    private fun loadReportData() {
        val report = dbHelper.getReportById(reportId)
        report?.let {
            binding.etDescription.setText(it.description)
            binding.etLocation.setText(it.location)
            binding.etContact.setText(it.contact)
            binding.etAddress.setText(it.address)

            if (it.photo.isNotEmpty()) {
                imageUri = Uri.parse(it.photo)
                Glide.with(requireContext())
                    .load(it.photo)
                    .into(binding.ivReportPhoto)
            } else {
                binding.ivReportPhoto.setImageResource(R.drawable.ic_photo)
            }
        }
    }

    private fun setupListeners() {
        binding.ivReportPhoto.setOnClickListener {
            showImagePickerDialog()
        }

        binding.btnUpload.setOnClickListener {
            saveReportChanges()
        }

        binding.btnCancel.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun saveReportChanges() {
        val description = binding.etDescription.text.toString().trim()
        val location = binding.etLocation.text.toString().trim()
        val contact = binding.etContact.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()

        if (description.isEmpty() || location.isEmpty() || contact.isEmpty() || address.isEmpty() || imageUri == null) {
            Toast.makeText(requireContext(), "Please fill in all fields and select an image!", Toast.LENGTH_SHORT).show()
            return
        }

        val photoPath = imageUri.toString()
        val rowsUpdated = dbHelper.updateReport(
            id = reportId,
            photo = photoPath,
            description = description,
            location = location,
            contact = contact,
            address = address,
            status = "Pending"
        )

        if (rowsUpdated > 0) {
            Toast.makeText(requireContext(), "Report successfully updated!", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        } else {
            Toast.makeText(requireContext(), "Failed to update report.", Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
