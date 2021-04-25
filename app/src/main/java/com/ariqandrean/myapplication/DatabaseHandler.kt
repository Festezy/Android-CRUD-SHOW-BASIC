package com.ariqandrean.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import java.util.ArrayList

class DatabaseHandler(context: Context) : SQLiteOpenHelper
    (context, DATABASE_NAME, null, DATABASE_VERSION) {

    // parameter database sqlite
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "EmployeeDatabase"

        private val TABLE_CONTACT = "EmployeeTable"

        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"
        private val KEY_PHONE = "phone"
        private val KEY_ADDR = "address"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // CREATE TABLE
        val CREATE_CONTACT_TABLE =
                ("CREATE TABLE " + TABLE_CONTACT + "("
                        + KEY_ID + " INTEGER PRIMARY KEY,"
                        + KEY_NAME + " TEXT,"
                        + KEY_EMAIL + " TEXT,"
                        + KEY_PHONE + " TEXT,"
                        + KEY_ADDR + " TEXT)")
        db?.execSQL(CREATE_CONTACT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACT")
        onCreate(db)
    }

    //method to insert data record
    fun addEmployee(emp: EmpModelClass): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, emp.name)
        contentValues.put(KEY_EMAIL, emp.email)
        contentValues.put(KEY_PHONE, emp.phone)
        contentValues.put(KEY_ADDR, emp.address)
        //inserting employee details using insert query
        val success = db.insert(TABLE_CONTACT, null, contentValues)
        //insert(TABLE_CONTACT, null, contentValues)
        db.close()
        return success
    }

    // Method to read the records
    fun viewEmployee(): ArrayList<EmpModelClass> {
        val empList: ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()
        val selectQuery = "SELECT * FROM $TABLE_CONTACT"

        val db = this.readableDatabase

        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var name: String
        var email: String
        var phone: String
        var address: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL))
                phone = cursor.getString(cursor.getColumnIndex(KEY_PHONE))
                address = cursor.getString(cursor.getColumnIndex(KEY_ADDR))
                val emp = EmpModelClass(id = id, name = name, email = email, phone = phone, address = address)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }

    //Method untuk menghapus data/record dalam database
    fun deleteEmployee(emp: EmpModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.id)

        val success = db.delete(TABLE_CONTACT, KEY_ID + "=" + emp.id, null)
        db.close()
        return success
    }
    // Method untuk mengupdate datarecord
    fun updateEmployee(emp: EmpModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, emp.name)
        contentValues.put(KEY_EMAIL, emp.email)
        contentValues.put(KEY_PHONE, emp.phone)
        contentValues.put(KEY_ADDR, emp.address)

        val success = db.update(TABLE_CONTACT, contentValues, KEY_ID + "=" + emp.id, null)
        db.close()
        return success
    }

}