package com.example.app.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.R
import com.example.app.data.db.DatabaseHelper
import com.example.app.data.db.Report
import com.example.app.databinding.FragmentReportListBinding

class ReportListFragment : Fragment() {

    private var _binding: FragmentReportListBinding? = null
    private val binding get() = _binding!!
    private lateinit var reportAdapter: ReportAdapter
    private val reportList: MutableList<Report> = mutableListOf()
    private val isAdmin: Boolean = true  // Setel ini sesuai dengan peran pengguna (admin atau user)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportListBinding.inflate(inflater, container, false)
        setupRecyclerView()
        loadReports()
        return binding.root
    }

    private fun setupRecyclerView() {
        reportAdapter = ReportAdapter(
            reports = reportList,
            isAdmin = isAdmin,  // Kirimkan informasi admin atau user
            onEditClickListener = { report -> navigateToEditReport(report) },
            onDeleteClickListener = { report -> deleteReport(report) },
            onStatusChangeListener = { report, newStatus ->
                if (isAdmin) {
                    updateReportStatus(report, newStatus)
                }
            }
        )

        binding.recyclerViewReports.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reportAdapter
        }
    }

    private fun loadReports() {
        val dbHelper = DatabaseHelper(requireContext())
        val reports = dbHelper.getAllReports()

        reportList.clear()
        reportList.addAll(reports)
        reportAdapter.notifyDataSetChanged()
    }

    private fun navigateToEditReport(report: Report) {
        val bundle = Bundle().apply {
            putInt("REPORT_ID", report.id)
            putBoolean("IS_ADMIN", isAdmin)  // Kirimkan status user atau admin
        }
        findNavController().navigate(R.id.action_adminReportFragment_to_adminEditReportFragment, bundle)
    }

    private fun deleteReport(report: Report) {
        val dbHelper = DatabaseHelper(requireContext())
        val rowsDeleted = dbHelper.deleteReport(report.id)
        if (rowsDeleted > 0) {
            reportList.remove(report)
            reportAdapter.notifyDataSetChanged()
        }
    }

    private fun updateReportStatus(report: Report, newStatus: String) {
        val dbHelper = DatabaseHelper(requireContext())
        val updatedRows = dbHelper.updateReportStatus(report.id, newStatus)
        if (updatedRows > 0) {
            report.status = newStatus
            reportAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
