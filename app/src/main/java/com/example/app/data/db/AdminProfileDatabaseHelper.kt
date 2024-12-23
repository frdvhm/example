package com.example.app.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminProfileDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "AppDatabase.db"
        private const val DATABASE_VERSION = 2 // Versi database

        const val TABLE_ADMIN_PROFILE = "AdminProfile"
        const val COLUMN_ADMIN_ID = "admin_id"
        const val COLUMN_ADMIN_NAME = "name"
        const val COLUMN_ADMIN_EMAIL = "email"
        const val COLUMN_ADMIN_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createAdminTableQuery = """
            CREATE TABLE $TABLE_ADMIN_PROFILE (
                $COLUMN_ADMIN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ADMIN_NAME TEXT,
                $COLUMN_ADMIN_EMAIL TEXT,
                $COLUMN_ADMIN_PASSWORD TEXT
            )
        """
        db.execSQL(createAdminTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Implementasi logika upgrade jika diperlukan
    }

    // CRUD untuk profil admin
    fun insertAdminProfile(name: String, email: String, password: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ADMIN_NAME, name)
            put(COLUMN_ADMIN_EMAIL, email)
            put(COLUMN_ADMIN_PASSWORD, password)
        }
        return db.insert(TABLE_ADMIN_PROFILE, null, values)
    }

    fun getAdminProfile(adminId: Int): AdminProfile? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_ADMIN_PROFILE, null, "$COLUMN_ADMIN_ID = ?", arrayOf(adminId.toString()),
            null, null, null
        )
        return if (cursor.moveToFirst()) {
            val admin = AdminProfile(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_NAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_EMAIL)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_PASSWORD)),
            )
            cursor.close()
            admin
        } else {
            cursor.close()
            null
        }
    }

    fun updateAdminProfile(adminId: Int, name: String, email: String, password: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ADMIN_NAME, name)
            put(COLUMN_ADMIN_EMAIL, email)
            put(COLUMN_ADMIN_PASSWORD, password)
        }
        return db.update(TABLE_ADMIN_PROFILE, values, "$COLUMN_ADMIN_ID = ?", arrayOf(adminId.toString()))
    }
}
