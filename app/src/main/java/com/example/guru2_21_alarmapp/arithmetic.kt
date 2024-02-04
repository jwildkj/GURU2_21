package com.example.guru2_21_alarmapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Random

class arithmetic : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var answerEditText: EditText
    private lateinit var checkAnswerButton: Button

    private var setArithmetic: SetArithmetic? = null
    private var correctAnswer: Int = 0
    private var operator: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arithmetic_quiz)

        questionTextView = findViewById(R.id.questionTextView)
        answerEditText = findViewById(R.id.answerEditText)
        checkAnswerButton = findViewById(R.id.checkAnswerButton)

        setArithmetic = intent.getSerializableExtra("setArithmetic") as? SetArithmetic

        generateQuestion()

        checkAnswerButton.setOnClickListener { checkAnswer() }
    }

    private fun generateQuestion() {
        val random = Random()
        val number1 = random.nextInt(100) + 1
        val number2 = random.nextInt(100) + 1

        //랜덤으로 연산자 선택
        val operators = arrayOf("+", "-", "*")
        operator = operators[random.nextInt(operators.size)]

        when (operator) {
            "+" -> correctAnswer = number1 + number2
            "-" -> {
                //뺄셈의 경우 음수가 나오지 않도록 처리
                correctAnswer = if (number1 >= number2) number1 - number2 else number2 - number1
            }
            "*" -> correctAnswer = number1 * number2
        }

        val question = "$number1 $operator $number2 = ?"
        questionTextView.text = question
    }

    private fun checkAnswer() {
        val userAnswer = answerEditText.text.toString().trim()

        if (userAnswer.isNotEmpty()) {
            val userIntAnswer = userAnswer.toInt()

            if (userIntAnswer == correctAnswer) {
                showToast("정답입니다!")
                //문제를 맞추면 SetArithmetic에 알림
                setArithmetic?.onAnswerCorrect()
            } else {
                showToast("틀렸습니다..")
                return
            }

            // 다음 문제 생성
            generateQuestion()
            // 사용자가 입력한 내용 초기화
            answerEditText.text.clear()
        } else {
            showToast("답을 입력해주세요.")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
