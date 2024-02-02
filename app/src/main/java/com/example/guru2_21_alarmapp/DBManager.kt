package com.example.guru2_21_alarmapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val DATABASE_NAME = "TimeDatabase"
const val TABLE_NAME = "TimeTable"
const val COL_ID = "id"
const val COL_AMPM = "ampm"
const val COL_HOUR = "hour"
const val COL_MINUTE = "minute"
const val COL_PROTYPE = "problem_type"

class DBManager(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE alarm (id INTEGER PRIMARY KEY AUTOINCREMENT, ampm INTEGER, hour INTEGER, minute INTEGER, problem_type TEXT)")
        //id는 자동 증가, 나머지는 삽입 예정
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun deleteAlarm(hour: Int, minute: Int): Boolean {
        val db = writableDatabase

        // 해당 시간과 분에 맞는 알람을 삭제
        val result = db.delete(
            "alarm",
            "$COL_HOUR=? AND $COL_MINUTE=?",
            arrayOf(hour.toString(), minute.toString())
        )

        db.close()

        // 삭제 결과에 따라 성공 여부 반환
        return result != 0
    }
}
