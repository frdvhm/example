package com.example.app.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.R
import com.example.app.data.db.DatabaseHelper
import com.example.app.data.db.Report
import com.example.app.databinding.FragmentAdminReportBinding
import com.example.app.ui.report.ReportAdapter

class AdminReportFragment : Fragment(R.layout.fragment_admin_report) {

    private var _binding: FragmentAdminReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var reportAdapter: ReportAdapter
    private val reportList: MutableList<Report> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminReportBinding.inflate(inflater, container, false)
        return binding.root // Cukup inflate dan kembalikan root view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        binding.recyclerViewReports.layoutManager = LinearLayoutManager(requireContext())

        // Initialize Adapter
        reportAdapter = ReportAdapter(
            reports = reportList,
            isAdmin = true,
            onEditClickListener = { report ->
                val bundle = Bundle().apply {
                    putInt("REPORT_ID", report.id)  // Ganti menjadi REPORT_ID
                }
                findNavController().navigate(R.id.action_adminReportFragment_to_adminEditReportFragment, bundle)
            },
            onDeleteClickListener = { report ->
                deleteReport(report)
            },
            onStatusChangeListener = { report, newStatus ->
                updateReportStatus(report, newStatus)
            }
        )
        binding.recyclerViewReports.adapter = reportAdapter

        // Load reports
        loadReports()
    }

    private fun loadReports() {
        val dbHelper = DatabaseHelper(requireContext())
        reportList.clear()
        reportList.addAll(dbHelper.getAllReports())  // Memuat semua laporan dari database
        reportAdapter.notifyDataSetChanged()
    }

    private fun deleteReport(report: Report) {
        val dbHelper = DatabaseHelper(requireContext())
        val rowsDeleted = dbHelper.deleteReport(report.id)
        if (rowsDeleted > 0) {
            reportList.remove(report)
            reportAdapter.notifyDataSetChanged()
            Toast.makeText(requireContext(), "Laporan berhasil dihapus", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Gagal menghapus laporan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateReportStatus(report: Report, newStatus: String) {
        val dbHelper = DatabaseHelper(requireContext())
        dbHelper.updateReport(
            id = report.id,
            photo = report.photo,
            description = report.description,
            location = report.location,
            contact = report.contact,
            address = report.address,
            status = newStatus
        )
        report.status = newStatus
        reportAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
