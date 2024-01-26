package com.example.guru2_21_alarmapp

import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.guru2_21_alarmapp.DBManager
import com.example.guru2_21_alarmapp.COL_AMPM
import com.example.guru2_21_alarmapp.COL_HOUR
import com.example.guru2_21_alarmapp.COL_MINUTE
import com.example.guru2_21_alarmapp.COL_NAME
import java.util.Calendar

class SetAlarm : AppCompatActivity() {

    lateinit var set_ampm: TextView
    lateinit var set_time: TextView
    lateinit var set_name: EditText
    lateinit var btn_cancel: Button
    lateinit var btn_save: Button
    lateinit var dbManager: DBManager
    lateinit var sqlitdb: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_alarm)

        set_ampm = findViewById(R.id.set_ampm)
        set_time = findViewById(R.id.set_time)
        set_name = findViewById(R.id.edt_name)
        btn_cancel = findViewById(R.id.btn_cancel)
        btn_save = findViewById(R.id.btn_save)

        dbManager = DBManager(this, "Alarm", null, 1)

//        set_ampm.setOnClickListener() {
//            changeAmpm() //(am/pm) 클릭 시 전환
//        }

        //시간 클릭 시
        set_time.setOnClickListener() {
            //Log.d("MainActivity", "time 클릭")
            showTimePicker() //TimePicker 보여주기
        }

        btn_cancel.setOnClickListener() {
            //그냥 초기 화면(목록 화면)으로 전환되게
            val intent = Intent(this@SetAlarm, MainActivity::class.java)
            startActivity(intent)
            finish() //현재 Activity 종료
        }

        btn_save.setOnClickListener() {
            saveAlarm() //DB에 저장
            //저장하고 목록으로 돌아가기
            val intent = Intent(this@SetAlarm, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showTimePicker() {
        Log.d("MainActivity", "show 그거 실행")
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val ampm: String //오전/오후
                val timeString: String //시간

                //오전/오후 & 12시간 처리
                if (hourOfDay < 12) {
                    ampm = "am"
                    timeString = String.format("%02d : %02d", hourOfDay, minute)
                }else if (hourOfDay == 12) {
                    ampm = "pm"
                    timeString = String.format("%02d : %02d", hourOfDay, minute)
                }else {
                    ampm = "pm"
                    timeString = String.format("%02d : %02d", hourOfDay - 12, minute)
                }

                //출력
                set_ampm.text = ampm
                set_time.text = timeString

            },
            currentHour,
            currentMinute,
            false
        )

        timePickerDialog.show()
    }

    private fun saveAlarm() {
        sqlitdb = dbManager.writableDatabase

        val ampm = if(set_ampm.text == "am") 0 else 1 //오전 0, 오후 1
        val timeParts = set_time.text.split(" : ") //시간 분리
        val hour = timeParts[0].toInt() //시
        val minute = timeParts[1].toInt() //분
        val name = set_name.text.toString() //이름

        //각 정보 contentValues에 추가
        val contentValues = ContentValues()
        contentValues.put(COL_NAME, name)
        contentValues.put(COL_AMPM, ampm)
        contentValues.put(COL_HOUR, hour)
        contentValues.put(COL_MINUTE, minute)

        //DB에 데이터 삽입
        val result = sqlitdb.insert("Alarm", null, contentValues)

        if (result < 0) {
            Toast.makeText(this, "저장에 실패했습니다.", Toast.LENGTH_SHORT).show() //저장 실패
        } else {
            Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show() //저장 성공
        }
    }

}