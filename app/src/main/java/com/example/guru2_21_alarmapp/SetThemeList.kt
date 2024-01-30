package com.example.guru2_21_alarmapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SetThemeList : AppCompatActivity() {
    lateinit var goSetPuls: Button
    lateinit var goSetWordQuiz: Button
    lateinit var goSetPhoto: Button
    lateinit var goSetStepCount: Button
    lateinit var goSetVoice: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_theme_list)

        goSetPuls = findViewById<Button>(R.id.goSetPlus)
        goSetWordQuiz = findViewById<Button>(R.id.goSetWordQuiz)
        goSetPhoto = findViewById<Button>(R.id.goSetPhoto)
        goSetStepCount =findViewById<Button>(R.id.goSetStepCount)
        goSetVoice = findViewById<Button>(R.id.goSetVoice)

        supportActionBar?.setTitle("주제 설정")

        goSetPuls.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)     // 아직 파일이 없어서 메인으로 설정
            startActivity(intent)
        }
        goSetWordQuiz.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)     // 단어 설정 페이지?
            startActivity(intent)
        }
        goSetPhoto.setOnClickListener {
            val intent = Intent(this, SetPhoto::class.java)
            startActivity(intent)
        }
        goSetStepCount.setOnClickListener {
            val intent = Intent(this, SetStepCount::class.java)
            startActivity(intent)
        }
        goSetVoice.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)     // 아직 파일이 없어서 메인으로 설정
            startActivity(intent)
        }
    }
}