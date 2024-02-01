package com.example.guru2_21_alarmapp

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup

class SetStepCount : AppCompatActivity() {
    // 걸음 수 설정 라디오 버튼들
    // 1 -> 10걸음, 2 -> 20걸음, 3 -> 직접설정
    lateinit var setStepCountRBs: RadioGroup
    lateinit var setStepCountRB1: RadioButton
    lateinit var setStepCountRB2: RadioButton
    lateinit var setStepCountRB3: RadioButton

    // 걸음 수 직접설정 EditText
    lateinit var setStepCountET1: EditText

    // 설정 저장, 확인 버튼
    lateinit var setStepCountButton: Button

    // 설정한 걸음 수를 저장하는 int변수
    private var setStepNum: Int? = null
    // 데이터베이스 이용시 활용할 텍스트
    private var setStepText: String = "걸음수"

    // 걸음수 데이터 베이스 활용
    lateinit var stepdbManager: stepDBManager
    lateinit var sqlitedb: SQLiteDatabase

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_step_count)

        // title bar
        supportActionBar?.setTitle("만보기")

        // 라디오 버튼
        setStepCountRBs = findViewById<RadioGroup>(R.id.setStepCountRBs)
        setStepCountRB1 = findViewById<RadioButton>(R.id.setStepCountRB1)
        setStepCountRB2 = findViewById<RadioButton>(R.id.setStepCountRB2)
        setStepCountRB3 = findViewById<RadioButton>(R.id.setStepCountRB3)

        // EditText
        setStepCountET1 = findViewById<EditText>(R.id.setStepCountET1)

        // Button
        setStepCountButton = findViewById<Button>(R.id.setStepCountButton)

        // 데이터베이스 생성
        stepdbManager = stepDBManager(this, "stepsetting", null, 1)
        sqlitedb = stepdbManager.writableDatabase
        sqlitedb.execSQL("INSERT INTO stepsetting VALUES ('"
                +setStepText+"', "
                +setStepNum+");")
        sqlitedb.close()

        // 걸음 수 설정
        setStepCountRBs.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.setStepCountRB1 -> {
                    setStepNum = 10
                    setStepCountET1.isEnabled = false
                }
                R.id.setStepCountRB2 -> {
                    setStepNum = 20
                    setStepCountET1.isEnabled = false
                }
                R.id.setStepCountRB3 -> {
                    // 직접 설정 버튼을 누를때만 edittext활성화
                    setStepCountET1.isEnabled = true
                }
            }
        }

        setStepCountButton.setOnClickListener {
            if (setStepCountET1.isEnabled == true) {
                if (setStepCountET1.text.toString() == "") {
                    // 직접 설정을 눌렀으나 숫자를 설정하지 않았을 경우, 임의의 목표치 30걸음 설정
                    setStepNum = 30
                } else {
                    setStepNum = setStepCountET1.text.toString().toInt()
                }
            }

            // 데이터베이스 업데이트, 수정
            sqlitedb = stepdbManager.writableDatabase
            sqlitedb.execSQL("UPDATE stepsetting SET goal = " + setStepNum + " WHERE step = '"
                    +setStepText+"';")
            sqlitedb.close()

            val intent2 = Intent(this, StepCount::class.java)
            intent2.putExtra("setStepText", setStepText)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}