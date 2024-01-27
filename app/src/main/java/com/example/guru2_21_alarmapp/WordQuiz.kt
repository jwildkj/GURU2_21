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
    )

    private var currentWord = ""
    private var currentOptions = listOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showRandomQuiz()

        findViewById<Button>(R.id.nextButton).setOnClickListener {
            showRandomQuiz()
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

