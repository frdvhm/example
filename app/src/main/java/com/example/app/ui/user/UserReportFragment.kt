package com.example.app.ui.user

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
import com.example.app.databinding.FragmentUserReportBinding
import com.example.app.ui.report.ReportAdapter

class UserReportFragment : Fragment(R.layout.fragment_user_report) {

    private var _binding: FragmentUserReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var reportAdapter: ReportAdapter
    private val reportList: MutableList<Report> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserReportBinding.inflate(inflater, container, false)



        binding.recyclerViewUser.layoutManager = LinearLayoutManager(requireContext())
        reportAdapter = ReportAdapter(
            reports = reportList,
            isAdmin = false,
            onEditClickListener = { report ->
                val bundle = Bundle().apply {
                    putInt("REPORT_ID", report.id)  // Ganti menjadi REPORT_ID
                }
                findNavController().navigate(R.id.action_userReportFragment_to_userEditReportFragment, bundle)
            },
            onDeleteClickListener = { report ->
                deleteReport(report)
            },
            onStatusChangeListener = { _, _ ->
                Toast.makeText(requireContext(), "User tidak diizinkan mengubah status", Toast.LENGTH_SHORT).show()
            }
        )

        binding.recyclerViewUser.adapter = reportAdapter
        loadReports()
        return binding.root
    }

    private fun loadReports() {
        val dbHelper = DatabaseHelper(requireContext())
        reportList.clear()
        reportList.addAll(dbHelper.getAllReports())
        reportAdapter.notifyDataSetChanged()
    }

    private fun deleteReport(report: Report) {
        val dbHelper = DatabaseHelper(requireContext())
        val rowsDeleted = dbHelper.deleteReport(report.id)
        if (rowsDeleted > 0) {
            Toast.makeText(requireContext(), "Laporan berhasil dihapus", Toast.LENGTH_SHORT).show()
            reportList.remove(report)
            reportAdapter.notifyDataSetChanged()
        } else {
            Toast.makeText(requireContext(), "Gagal menghapus laporan", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
