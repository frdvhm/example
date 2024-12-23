package com.example.app.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.app.data.db.DatabaseHelper
import com.example.app.databinding.FragmentAdminEditReportBinding

class AdminEditReportFragment : Fragment() {

    private var _binding: FragmentAdminEditReportBinding? = null
    private val binding get() = _binding!!
    private var reportId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminEditReportBinding.inflate(inflater, container, false)

        // Get the report ID from arguments
        reportId = arguments?.getInt("REPORT_ID") ?: -1

        // Load report details
        loadReportDetails()

        // Setup status dropdown
        val statusOptions = listOf("Pending", "On Progress", "Complete")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, statusOptions)
        binding.spinnerStatus.adapter = adapter

        binding.btnSave.setOnClickListener {
            saveChanges()
        }

        return binding.root
    }

    private fun loadReportDetails() {
        val dbHelper = DatabaseHelper(requireContext())
        val report = dbHelper.getReportById(reportId)
        report?.let {
            binding.etDescription.setText(it.description)
            binding.etLocation.setText(it.location)
            binding.spinnerStatus.setSelection(listOf("Pending", "On Progress", "Complete").indexOf(it.status))
        }
    }

    private fun saveChanges() {
        val dbHelper = DatabaseHelper(requireContext())

        val updatedRows = dbHelper.updateReport(
            reportId,
            photo = "", // Assume photo remains unchanged
            description = binding.etDescription.text.toString(),
            location = binding.etLocation.text.toString(),
            contact = "",
            address = "",
            status = binding.spinnerStatus.selectedItem.toString()
        )

        if (updatedRows > 0) {
            Toast.makeText(requireContext(), "Laporan berhasil diperbarui", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        } else {
            Toast.makeText(requireContext(), "Gagal memperbarui laporan", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
