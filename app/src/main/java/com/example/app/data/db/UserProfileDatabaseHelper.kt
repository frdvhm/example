package com.example.app.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserProfileDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserProfile.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "users"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_USERS (
                id INTEGER PRIMARY KEY,
                name TEXT,
                email TEXT,
                password TEXT,
                dob TEXT,
                location TEXT
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun getUserProfile(userId: Int): UserProfile? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            null,
            "id = ?",
            arrayOf(userId.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            UserProfile(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                dob = cursor.getString(cursor.getColumnIndexOrThrow("dob")),
                location = cursor.getString(cursor.getColumnIndexOrThrow("location"))
            )
        } else {
            null
        }.also {
            cursor.close()
        }
    }

    fun updateUserProfile(userId: Int, name: String, email: String, password: String, dob: String, location: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("email", email)
            put("password", password)
            put("dob", dob)
            put("location", location)
        }

        return db.update(TABLE_USERS, values, "id = ?", arrayOf(userId.toString()))
    }
}