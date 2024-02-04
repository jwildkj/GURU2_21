package com.example.guru2_21_alarmapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
//import com.example.guru2_21_alarmapp

class RingAlarm : AppCompatActivity() {

    lateinit var txv_ampm: TextView
    lateinit var txv_time: TextView
    lateinit var btn_solve: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ring_alarm)

        txv_ampm = findViewById(R.id.txv_ampm)
        txv_time = findViewById(R.id.txv_time)
        btn_solve = findViewById(R.id.btn_solve)

        // Intent에서 정보 추출
        val ampm = intent.getIntExtra("ampm", 0) // 0: am, 1: pm
        val hour = intent.getIntExtra("hour", 0)
        val minute = intent.getIntExtra("minute", 0)
        val problem = intent.getStringExtra("proType_name")

        btn_solve.setOnClickListener {
            when (problem) {
                "사칙연산" -> {
                    val intent = Intent(this, arithmetic::class.java) //이동할 액티비티 값 가져오기
                    startActivity(intent)
                    finish() //현재 액티비티 종료
                }
                "단어 퀴즈" -> {
                    val intent = Intent(this, WordQuiz::class.java) //이동할 액티비티 값 가져오기
                    startActivity(intent)
                    finish() //현재 액티비티 종료
                }
                "사진" -> {
                    val intent = Intent(this, TakePhoto::class.java) //이동할 액티비티 값 가져오기
                    startActivity(intent)
                    finish() //현재 액티비티 종료
                }
                "만보기" -> {
                    val intent = Intent(this, StepCount::class.java) //이동할 액티비티 값 가져오기
                    startActivity(intent)
                    finish() //현재 액티비티 종료
                }
                "음성 인식" -> {
                    val intent = Intent(this, VoiceActivity::class.java) //이동할 액티비티 값 가져오기
                    startActivity(intent)
                    finish() //현재 액티비티 종료
                }
                else -> {
                    val intent = Intent(this, arithmetic::class.java) //이동할 액티비티 값 가져오기
                    startActivity(intent)
                    finish() //현재 액티비티 종료
                }
        }

        }

    }
}