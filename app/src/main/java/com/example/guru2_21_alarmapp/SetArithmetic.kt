package com.example.guru2_21_alarmapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity

class SetArithmetic : AppCompatActivity() {
    //설정한 문제 수를 저장하는 변수
    private var totalQuestions: Int = 0
    private var answeredQuestions: Int = 0

    private lateinit var setQuizCountRBs: RadioGroup
    private lateinit var setQuizCountRB4: RadioButton
    private lateinit var setStepCountET1: EditText
    private lateinit var setStepCountButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_arithmetic)

        setQuizCountRBs = findViewById(R.id.setQuizCountRBs)
        setQuizCountRB4 = findViewById(R.id.setQuizCountRB4)
        setStepCountET1 = findViewById(R.id.setStepCountET1)
        setStepCountButton = findViewById(R.id.setStepCountButton)

        //직접 설정 선택 시 EditText 활성화
        setQuizCountRBs.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.setQuizCountRB1 -> {
                    totalQuestions = 1
                    setStepCountET1.isEnabled = false
                }
                R.id.setQuizCountRB2 -> {
                    totalQuestions = 2
                    setStepCountET1.isEnabled = false
                }
                R.id.setQuizCountRB3 -> {
                    totalQuestions = 3
                    setStepCountET1.isEnabled = false
                }
                R.id.setQuizCountRB4 -> {
                    setStepCountET1.isEnabled = true
                }
            }
        }

        // 결정 버튼 클릭 시
        setStepCountButton.setOnClickListener {
            if (setStepCountET1.isEnabled) {
                val stepCount = setStepCountET1.text.toString().toIntOrNull()
                if (stepCount != null && stepCount > 0) {
                    totalQuestions = stepCount
                } else {
                    // 올바른 값을 입력하지 않았을 경우 기본값 1로 설정
                    totalQuestions = 1
                }
            }

            val resultIntent = Intent()
            resultIntent.putExtra("totalQuestions", totalQuestions)
            setResult(Activity.RESULT_OK, resultIntent)

            // 액티비티 종료
            finish()
        }
    }

    // 끄기 버튼 활성화 함수
    private fun enableQuitButton() {
        val quitBtn: Button = findViewById(R.id.quitBtn)
        quitBtn.isEnabled = true
        quitBtn.setOnClickListener {
            // 메인 액티비티로 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // 문제를 맞췄을 때 호출하는 함수
    fun onAnswerCorrect() {
        answeredQuestions++

        if (answeredQuestions >= totalQuestions) {
            // 모든 문제를 맞추면 끄기 버튼 활성화
            enableQuitButton()
        }
    }
}
