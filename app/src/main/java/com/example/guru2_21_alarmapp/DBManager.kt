package com.example.guru2_21_alarmapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val DATABASE_NAME = "TimeDatabase"
const val TABLE_NAME = "TimeTable"
const val COL_ID = "ID"
const val COL_AMPM = "AMPM"
const val COL_HOUR = "Hour"
const val COL_MINUTE = "Minute"

class DBManager(
    context: Context?,
    name:String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
    ):SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE alarm (id INTEGER PRIMARY KEY AUTOINCREMENT, ampm INTEGER, hour INTEGER, minute INTEGER)")
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
}