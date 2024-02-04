package com.example.guru2_21_alarmapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.Locale
import kotlin.random.Random

class VoiceActivity : AppCompatActivity() {
    lateinit var sentence_textview: TextView
    lateinit var start_button:Button
    private val REQUEST_CODE_SPEECH = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice)

        sentence_textview = findViewById<TextView>(R.id.sentenceTextView)
        start_button=findViewById(R.id.startButton)

        setRandomSentence()

        start_button.setOnClickListener {
            startVoiceRecognition()
        }
    }
    private fun setRandomSentence(){
        val sentences = arrayOf(
            "나는 아침에 일어나면 항상 미소를 짓는다.",
            "오전 열 시까지 즐거운 마음을 가져라.",
            "주사위는 던져졌다.",
            "그래도 지구는 돈다.",
            "진정 웃으려면 고통을 참아야 하며, 나아가 고통을 즐길 줄 알아야 한다.",
            "피할 수 없으면 즐겨라.",
            "오랫동안 꿈을 그리는 사람은 마침내 그 꿈을 닮아간다.",
            "자신감 있는 표정을 지으면 자신감이 생긴다.",
            "삶은 소유물이 아니라 순간순간의 있음이다.",
            "자신을 내보여라. 그러면 재능이 드러날 것이다.",
            "문제점을 찾지 말고 해결책을 찾으라.",
            "우리는 두려움의 홍수에 버티기 위해서 끊임없이 용기의 둑을 쌓아야 한다.",
            "길을 잃는다는 것은 곧 길을 알게 된다는 것이다."
        )
        val randomIndex = Random.nextInt(sentences.size)
        val randomSentence = sentences[randomIndex]
        sentence_textview.text = randomSentence

    }
    private fun startVoiceRecognition() {
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "말해주세요...")

        try {
            startActivityForResult(speechRecognizerIntent, REQUEST_CODE_SPEECH)
        } catch (e: Exception) {
            Toast.makeText(this, "음성 인식을 지원하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH && resultCode == RESULT_OK) {
            val matches =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            matches?.let {
                val userSpokenText = it[0]
                // 사용자가 말한 문장과 랜덤으로 선택된 문장이 일치하는지 확인
                if (userSpokenText == sentence_textview.text.toString()) {
                    Toast.makeText(this, "일치합니다!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "일치하지 않습니다. 다시 시도하세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}