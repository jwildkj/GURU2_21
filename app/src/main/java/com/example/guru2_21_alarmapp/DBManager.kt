package com.example.alarmtest_05

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val DATABASE_NAME = "TimeDatabase"
const val TABLE_NAME = "TimeTable"
const val db_id = "ID"
const val DB_AMPM = "AMPM"
const val DB_HOUR = "Hour"
const val DB_MINUTE = "Minute"
const val DB_NAME = "name"

class DBManager(
    context: Context?,
    name:String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
):SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL ("CREATE TABLE alarm (id INTEGER PRIMARY KEY AUTOINCREMENT, name string, ampm INTEGER, hour INTEGER, minute INTEGER)")
        //id는 자동 증가, 나머지는 삽입 예정
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
}