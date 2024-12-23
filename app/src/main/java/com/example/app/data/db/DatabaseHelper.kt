package com.example.app.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "AppDatabase.db"
        private const val DATABASE_VERSION = 2 // Versi database

        const val TABLE_NAME = "Reports"
        const val COLUMN_ID = "id"
        const val COLUMN_PHOTO = "photo"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_LOCATION = "location"
        const val COLUMN_CONTACT = "contact"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createReportsTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PHOTO TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_LOCATION TEXT,
                $COLUMN_CONTACT TEXT,
                $COLUMN_ADDRESS TEXT,
                $COLUMN_STATUS TEXT
            )
        """
        db.execSQL(createReportsTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Implementasi logika upgrade jika ada
    }

    // CRUD untuk laporan
    fun insertReport(
        photo: String,
        description: String,
        location: String,
        contact: String,
        address: String,
        status: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PHOTO, photo)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_LOCATION, location)
            put(COLUMN_CONTACT, contact)
            put(COLUMN_ADDRESS, address)
            put(COLUMN_STATUS, status)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllReports(): List<Report> {
        val db = readableDatabase
        val reports = mutableListOf<Report>()
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, "$COLUMN_ID DESC")

        if (cursor.moveToFirst()) {
            do {
                val report = Report(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    photo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHOTO)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                    contact = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTACT)),
                    address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                    status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)) ?: "Pending"
                )
                reports.add(report)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return reports
    }

    fun getReportById(reportId: Int): Report? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME, null, "$COLUMN_ID = ?", arrayOf(reportId.toString()),
            null, null, null
        )
        return if (cursor.moveToFirst()) {
            val report = Report(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                photo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHOTO)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                contact = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTACT)),
                address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
                status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)) ?: "Pending"
            )
            cursor.close()
            report
        } else {
            cursor.close()
            null
        }
    }

    fun deleteReport(reportId: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(reportId.toString()))
    }

    fun updateReport(
        id: Int,
        photo: String,
        description: String,
        location: String,
        contact: String,
        address: String,
        status: String
    ): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PHOTO, photo)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_LOCATION, location)
            put(COLUMN_CONTACT, contact)
            put(COLUMN_ADDRESS, address)
            put(COLUMN_STATUS, status)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    // Menambahkan metode updateReportStatus untuk memperbarui status laporan
    fun updateReportStatus(reportId: Int, status: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_STATUS, status)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(reportId.toString()))
    }
}
