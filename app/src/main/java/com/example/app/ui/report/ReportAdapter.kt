package com.example.app.ui.report

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.app.R
import com.example.app.data.db.Report

class ReportAdapter(
    private val reports: MutableList<Report>,
    private val isAdmin: Boolean,
    private val onEditClickListener: (Report) -> Unit,
    private val onDeleteClickListener: (Report) -> Unit,
    private val onStatusChangeListener: (Report, String) -> Unit
) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reports[position]
        holder.bind(report)

        // Menangani klik tombol Edit
        holder.ivEdit.setOnClickListener {
            onEditClickListener(report)  // Tangani navigasi untuk Admin dan User
        }

        // Menangani klik tombol Hapus
        holder.ivDelete.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setMessage("Apakah Anda yakin ingin menghapus laporan ini?")
                .setPositiveButton("Ya") { dialog, _ ->
                    onDeleteClickListener(report)
                    dialog.dismiss()
                }
                .setNegativeButton("Batal", null)
                .show()
        }

        // Hanya admin yang bisa mengubah status laporan
        if (isAdmin) {
            holder.spinnerStatus.visibility = View.VISIBLE
            val statusOptions = listOf("Pending", "On Progress", "Complete")
            holder.spinnerStatus.setSelection(statusOptions.indexOf(report.status))
            holder.spinnerStatus.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedStatus = statusOptions[position]
                    onStatusChangeListener(report, selectedStatus)
                }

                override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
            }
        } else {
            holder.spinnerStatus.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = reports.size

    fun removeItem(report: Report) {
        val position = reports.indexOf(report)
        if (position != -1) {
            reports.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateData(newReports: List<Report>) {
        reports.clear()
        reports.addAll(newReports)
        notifyDataSetChanged()
    }

    inner class ReportViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ivReportPhoto: ImageView = view.findViewById(R.id.ivReportPhoto)
        private val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        private val tvLocation: TextView = view.findViewById(R.id.tvLocation)
        private val tvContact: TextView = view.findViewById(R.id.tvContact)
        private val tvAddress: TextView = view.findViewById(R.id.tvAddress)
        val ivEdit: ImageView = view.findViewById(R.id.ivEdit)
        val ivDelete: ImageView = view.findViewById(R.id.ivDelete)
        val spinnerStatus: Spinner = view.findViewById(R.id.spinnerStatus)

        fun bind(report: Report) {
            tvDescription.text = report.description
            tvLocation.text = report.location
            tvContact.text = report.contact
            tvAddress.text = report.address

            if (report.photo.isNotEmpty()) {
                Glide.with(itemView.context).load(report.photo).into(ivReportPhoto)
            } else {
                ivReportPhoto.setImageResource(R.drawable.ic_photo)
            }
        }
    }
}
