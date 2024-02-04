package com.example.guru2_21_alarmapp

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class WordQuiz : AppCompatActivity() {

    private val quizData = mutableMapOf(

        "Apple" to listOf("사과", "바나나", "딸기"),
        "Banana" to listOf("바나나", "사과", "오렌지"),
        "Orange" to listOf("오렌지", "사과", "바나나"),
        "Doctor" to listOf("의사", "간호사", "나무꾼"),
        "Dog" to listOf("개", "고양이", "거북이"),
        "Car" to listOf("자동차", "오토바이", "자전거"),
        "Book" to listOf("책", "신문", "잡지"),
        "Computer" to listOf("컴퓨터", "노트북", "스마트폰"),
        "Rain" to listOf("비", "눈", "안개"),
        "Mountain" to listOf("산", "바다", "육지")
    )

    private var currentWord = ""
    private var currentOptions = listOf<String>()
    private var answeredQuizCount = 0 // 사용자가 푼 문제 수
    private var quizCountLimit = 0 // 사용자가 설정한 문제 수 제한

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.word_quiz)

        showRandomQuiz()

        findViewById<Button>(R.id.nextButton).setOnClickListener {
            if (answeredQuizCount < quizCountLimit) {
                showRandomQuiz()
                answeredQuizCount++
            } else {
                //사용자가 설정한 문제 수를 모두 풀었을 때 끄기 버튼 활성화
                findViewById<Button>(R.id.ExitButton).isEnabled = true
            }
        }
    }

    private fun showRandomQuiz() {
        val randomIndex = Random.nextInt(quizData.size)
        val (word, options) = quizData.entries.elementAt(randomIndex)

        currentWord = word
        currentOptions = options.shuffled()

        findViewById<TextView>(R.id.wordTextView).text = currentWord

        val optionLayout = findViewById<LinearLayout>(R.id.optionsLayout)
        optionLayout.removeAllViews()

        for (option in currentOptions) {
            val optionButton = Button(this)
            optionButton.text = option
            optionButton.setOnClickListener { checkAnswer(option) }

            optionLayout.addView(optionButton)
        }
    }

    private fun checkAnswer(selectedOption: String) {
        val correctOption = quizData[currentWord]?.get(0)

        if (selectedOption == correctOption) {
            showToast("정답입니다!")
        } else {
            showToast("틀렸습니다.")
            //틀렸을 때는 다음 문제로 넘어가지 X
            return
        }

        showRandomQuiz()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

