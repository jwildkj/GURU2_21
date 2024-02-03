package com.example.guru2_21_alarmapp

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    lateinit var alarmListView: ListView
    lateinit var alarmList: ArrayList<String>
    lateinit var dbManager: DBManager

    var currentTime: Long = System.currentTimeMillis()      // 현재 시간
    var dataFormat = SimpleDateFormat("hh : mm")     // 현재 시간 "시간 : 분" 형식으로
    val mDelayHandler: Handler by lazy { Handler() }        // 시간 갱신을 위한 handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmListView = findViewById(R.id.alarmListView)
        dbManager = DBManager(this, DATABASE_NAME, null, 1)

        //알람 데이터베이스에서 알람 목록 가져오기
        alarmList = getAllAlarms()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, alarmList)
        alarmListView.adapter = adapter

        alarmListView.setOnItemLongClickListener { _, _, position, _ ->
            //알람 삭제 확인
            showDeleteConfirmationDialog(position)
            true
        }

        // 시간갱신
        showRingAlarm()

        // 보행 탐지기 권한 허용
        if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "No permission!!", Toast.LENGTH_SHORT).show()

            // ask for permission
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION))
        }
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
            val hour = data?.getIntExtra("hour", 0) ?: 0
            val minute = data?.getIntExtra("minute", 0) ?: 0
            val problemType = data?.getStringExtra("problemType") ?: "사칙연산"

            //알람 시간과 문제
            val newAlarm = String.format("%02d : %02d - $problemType", hour, minute)
            alarmList.add(newAlarm)

            // 리스트뷰 갱신
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
                val intent = Intent(this, SetThemeList::class.java)
                startActivity(intent)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("알람을 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                deleteAlarm(position)
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }


    private fun deleteAlarm(position: Int) {
        val selectedAlarm = alarmList[position]

        val parts = selectedAlarm.split(" - ")
        val timeString = parts[0]

        val timeParts = timeString.split(" : ")
        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()

        //데이터베이스에서 해당 알람을 삭제
        if (dbManager.deleteAlarm(hour, minute)) {
            //업데이트
            alarmList.removeAt(position)
            (alarmListView.adapter as ArrayAdapter<String>).notifyDataSetChanged()
            Toast.makeText(this, "알람이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            // 삭제 실패 시 메시지 출력
            Toast.makeText(this, "알람 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 시간 갱신
    private fun waitNext() {
        mDelayHandler.postDelayed(::showRingAlarm, 1000)    // 1초마다 showRingAlarm() 실행
    }

    // 시간 확인
    private fun showRingAlarm() {
        var mCheckTime = checkTime()
        currentTime = System.currentTimeMillis()    // 시간 갱신

        if (mCheckTime != true){
            waitNext()
        }
    }

    // 알람 리스트와 현재 시간을 비교
    // 만약 알람 리스트에 있는 시간과 현재 시간이 같으면 ringalarm 화면으로 넘어감 + 설정한 퀴즈방식을 intent로 넘김
    private fun checkTime(): Boolean {
        val alarmData = dbManager.readableDatabase.rawQuery("SELECT * FROM alarm", null)
        var alarms = ArrayList<String>()            // 알람
        var protypes = ArrayList<String>()          // 해당 알람에 설정한 퀴즈방식

        if (alarmData.moveToFirst()) {
            do {
                val colHour = alarmData.getColumnIndex(COL_HOUR)
                val colMinute = alarmData.getColumnIndex(COL_MINUTE)
                val colProtype = alarmData.getColumnIndex(COL_PROTYPE)

                if (colHour != -1 && colMinute != -1) {
                    val hour = alarmData.getInt(colHour)
                    val minute = alarmData.getInt(colMinute)
                    val protype = alarmData.getString(colProtype)
                    val alarmTime = String.format("%02d : %02d", hour, minute)
                    alarms.add(alarmTime)
                    protypes.add(protype)
                }
            } while (alarmData.moveToNext())
        }
        alarmData.close()

        for (i in 0 until alarms.count()){
            // 현재시간과 설정한 시간을 확인
            if (dataFormat.format(currentTime) == alarms[i]) {
                val proType_name = protypes[i]
                val intent = Intent(this, RingAlarm::class.java)
                intent.putExtra("proType_name", proType_name)
                startActivity(intent)

                return true
            }
        }

        return false
    }

    // 권한 허용 요청
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) {

        var results = true
        it.values.forEach {
            if (it == false) {
                results = false
                return@forEach
            }
        }

        if (!results) {
            Toast.makeText(this@MainActivity, "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }
}