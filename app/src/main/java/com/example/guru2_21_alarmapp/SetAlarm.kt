package com.example.guru2_21_alarmapp

import android.app.Activity
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class SetAlarm : AppCompatActivity() {

    lateinit var set_ampm: TextView
    lateinit var set_time: TextView
    lateinit var set_name: EditText
    lateinit var btn_cancel: Button
    lateinit var btn_save: Button
    lateinit var dbManager: DBManager
    lateinit var radioButton1: RadioButton
    lateinit var radioButton2: RadioButton
    lateinit var radioButton3: RadioButton
    lateinit var radioButton4: RadioButton
    lateinit var radioButton5: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_alarm)

        set_ampm = findViewById(R.id.set_ampm)
        set_time = findViewById(R.id.set_time)
        set_name = findViewById(R.id.edt_name)
        btn_cancel = findViewById(R.id.btn_cancel)
        btn_save = findViewById(R.id.btn_save)
        radioButton1 = findViewById(R.id.radioButton1)
        radioButton2 = findViewById(R.id.radioButton2)
        radioButton3 = findViewById(R.id.radioButton3)
        radioButton4 = findViewById(R.id.radioButton4)
        radioButton5 = findViewById(R.id.radioButton5)


        //시간 클릭 시
        set_time.setOnClickListener() {
            showTimePicker() //TimePicker 보여주기
        }

        btn_cancel.setOnClickListener() {
            //그냥 초기 화면(목록 화면)으로 전환되게
            finish()
        }

        btn_save.setOnClickListener() {
            saveAlarm() //DB에 저장
        }
    }

    private fun showTimePicker() {
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
                } else if (hourOfDay == 12) {
                    ampm = "pm"
                    timeString = String.format("%02d : %02d", hourOfDay, minute)
                } else {
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
        val dbManager = DBManager(this, DATABASE_NAME, null, 1)
        val db = dbManager.writableDatabase

        val ampm = if (set_ampm.text == "am") 0 else 1 // 오전오후 구분 0=am, 1=pm
        val timeParts = set_time.text.split(" : ")
        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()
        val name = set_name.text.toString()

        val contentValues = ContentValues()
        contentValues.put(COL_NAME, name)
        contentValues.put(COL_AMPM, ampm)
        contentValues.put(COL_HOUR, hour)
        contentValues.put(COL_MINUTE, minute)

        val result = db.insert("Alarm", null, contentValues)

        if (result == -1L) {
            // 저장 실패
            Toast.makeText(this, "저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
            // 디버그 로그
            Log.e("SetAlarm", "Insert failed: $result")
        } else {
            // 저장 성공
            Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()

            // 저장 성공 시, 결과 설정 및 현재 액티비티 종료
            val intent = Intent()
            intent.putExtra("hour", hour)
            intent.putExtra("minute", minute)
            intent.putExtra("problemType", SelectQuiz())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        db.close()
    }

    private fun SelectQuiz(): String {
        return when {
            radioButton1.isChecked -> "사칙연산"
            radioButton2.isChecked -> "단어 퀴즈"
            radioButton3.isChecked -> "사진"
            radioButton4.isChecked -> "만보기"
            radioButton5.isChecked -> "음성 인식"
            else -> "사칙연산" //기본값
        }
    }

}