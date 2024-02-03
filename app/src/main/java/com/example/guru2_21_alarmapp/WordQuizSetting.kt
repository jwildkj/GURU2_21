package com.example.guru2_21_alarmapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class WordQuizSetting : AppCompatActivity() {

    var selectedHour: Int = 0
    var selectedMinute: Int = 0
    var quizCountLimit: Int = 0

    lateinit var setQuizCountRBs: RadioGroup
    lateinit var setQuizCountRB1: RadioButton
    lateinit var setQuizCountRB2: RadioButton
    lateinit var setQuizCountRB3: RadioButton
    lateinit var setQuizCountRB4: RadioButton
    lateinit var setStepCountET: LinearLayout
    lateinit var setStepCountET1: EditText
    lateinit var setStepCountET2: TextView
    lateinit var setStepCountButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wordquiz_setting)

        setQuizCountRBs = findViewById(R.id.setQuizCountRBs)
        setQuizCountRB1 = findViewById(R.id.setQuizCountRB1)
        setQuizCountRB2 = findViewById(R.id.setQuizCountRB2)
        setQuizCountRB3 = findViewById(R.id.setQuizCountRB3)
        setQuizCountRB4 = findViewById(R.id.setQuizCountRB4)
        setStepCountET = findViewById(R.id.setStepCountET)
        setStepCountET1 = findViewById(R.id.setStepCountET1)
        setStepCountET2 = findViewById(R.id.setStepCountET2)
        setStepCountButton = findViewById(R.id.setStepCountButton)

        setQuizCountRBs.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.setQuizCountRB4 -> {
                    setStepCountET1.isEnabled = true //직접 설정 선택 시 EditText 활성화
                }
                else -> {
                    setStepCountET1.isEnabled = false //다른 항목 선택 시 EditText 비활성화
                }
            }
        }

        setStepCountButton.setOnClickListener {
            val selectedRadioButtonId = setQuizCountRBs.checkedRadioButtonId
            var selectedQuizCount = 1 //기본값 1

            when (selectedRadioButtonId) {
                R.id.setQuizCountRB1 -> selectedQuizCount = 1
                R.id.setQuizCountRB2 -> selectedQuizCount = 2
                R.id.setQuizCountRB3 -> selectedQuizCount = 3
                R.id.setQuizCountRB4 -> {
                    val userInput = setStepCountET1.text.toString()
                    if (userInput.isNotEmpty()) {
                        selectedQuizCount = userInput.toInt()
                    }
                }
            }

            quizCountLimit = selectedQuizCount

            // 액티비티 호출 측에 데이터 반환
            val intent = Intent()
            intent.putExtra("selectedHour", selectedHour)
            intent.putExtra("selectedMinute", selectedMinute)
            intent.putExtra("quizCountLimit", quizCountLimit)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
