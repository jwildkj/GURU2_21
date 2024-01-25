package com.example.guru2_21_alarmapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var alarmListView: ListView
    lateinit var alarmList: ArrayList<String>
    lateinit var dbManager: DBManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmListView = findViewById(R.id.alarmListView)
        dbManager = DBManager(this, DATABASE_NAME, null, 1)

        //알람 데이터베이스에서 알람 목록 가져오기
        alarmList = getAllAlarms()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, alarmList)
        alarmListView.adapter = adapter

        alarmListView.setOnItemClickListener(AdapterView.OnItemClickListener { _, _, position, _ ->
            // 선택한 알람 수정?
        })
    }

    private fun getAllAlarms(): ArrayList<String> {
        val alarmData = dbManager.readableDatabase.rawQuery("SELECT * FROM alarm", null)
        val alarms = ArrayList<String>()

        if (alarmData.moveToFirst()) {
            do {
                val colAMPM = alarmData.getColumnIndex(COL_AMPM)
                val colHour = alarmData.getColumnIndex(COL_HOUR)
                val colMinute = alarmData.getColumnIndex(COL_MINUTE)

                if (colAMPM != -1 && colHour != -1 && colMinute != -1) {
                    val ampm = if (alarmData.getInt(colAMPM) == 0) "am" else "pm"
                    val hour = alarmData.getInt(colHour)
                    val minute = alarmData.getInt(colMinute)
                    val alarmTime = String.format("%02d : %02d $ampm", hour, minute)
                    alarms.add(alarmTime)
                }
            } while (alarmData.moveToNext())
        }

        alarmData.close()
        return alarms
    }

    // 알람을 추가할 때 요청 코드
    companion object {
        const val ADD_ALARM_REQUEST_CODE = 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_ALARM_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //SetAlarm에서 설정한 알람 메인에 표시
            val hour = data?.getIntExtra("hour", 0) ?: 0
            val minute = data?.getIntExtra("minute", 0) ?: 0

            // 알람 리스트에 추가
            val newAlarm = String.format("%02d : %02d", hour, minute)
            alarmList.add(newAlarm)

            // 어댑터에 변경된 데이터를 알려서 리스트뷰 갱신
            (alarmListView.adapter as ArrayAdapter<String>).notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.alarmlist, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_alarm -> {
                //알람 추가 선택
                val intent = Intent(this, SetAlarm::class.java)
                startActivityForResult(intent, ADD_ALARM_REQUEST_CODE)
                return true
            }
            R.id.sub_set -> {
                //주제 설정 선택
                val intent = Intent(this, SubSetting::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
