package com.example.guru2_21_alarmapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class StepCount : AppCompatActivity(), SensorEventListener {
    lateinit var stepdbManager: stepDBManager
    lateinit var sqlitedb: SQLiteDatabase

    // 현재 걸음수 textview
    lateinit var tv_stepCount: TextView

    // 끄기 버튼
    lateinit var stepCountButton: Button

    // 센서 연결을 위한 변수
    lateinit var sensorManager: SensorManager
    lateinit var sensor_stepCount: Sensor
    private val TYPE = Sensor.TYPE_STEP_COUNTER     // 보행 계수기

    private var mSteps: Int = 0             // 현재 걸음수
    private var mCounterSteps: Int = 0      // 리스너가 등록되고 난 후의 step count
    private var setStepNum: Int = 10        // 설정한 목표치 걸음수, 기본값 10
    lateinit var setStepText: String        // SetStepCount에서 intent로 넘긴 "걸음수"를 받는 변수

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_count)

        // title bar
        supportActionBar?.setTitle("만보기")

        // 현재 걸음수를 보여줄 textview
        tv_stepCount = findViewById<TextView>(R.id.stepCountText)
        tv_stepCount.setText("0")       // 처음 화면에 띄울 숫자는 0

        // 끄기 버튼
        stepCountButton = findViewById<Button>(R.id.stepCountButton)

        // SetStepCount에서 넘긴 값 받기
        val intent = intent
        setStepText = intent.getStringExtra("setStepText").toString()

        // 센서 연결 (걸음 수 센서를 이용한 흔듬 감지)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager   // 센서 매니저 생성
        sensor_stepCount = sensorManager.getDefaultSensor(TYPE)!!                   // 스텝 감지 센서

        // 데이터베이스에서 값 받기
        stepdbManager = stepDBManager(this, "stepsetting", null, 1)
        sqlitedb = stepdbManager.readableDatabase

        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM stepsetting WHERE step = '"+setStepText+"';", null)

        if(cursor.moveToNext()) {
            setStepNum = cursor.getInt(cursor.getColumnIndex("goal"))
        }
        cursor.close()
        sqlitedb.close()
        stepdbManager.close()

        // 끄기 버튼 설정
        stepCountButton.setOnClickListener {
            // 초기화
            mSteps = 0
            mCounterSteps = 0
            tv_stepCount.text = mSteps.toString()
            stepCountButton.isEnabled = false

            // 메인화면으로 돌아감
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // 센서 속도 설정
    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensor_stepCount, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    // 만보기 걸음수 업데이트
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == TYPE) {
            if (mCounterSteps < 1) {
                // 초기 값
                mCounterSteps = event.values[0].toInt()
            }
            // 현재 걸음수
            mSteps = event.values[0].toInt() - mCounterSteps
            tv_stepCount.text = mSteps.toString()
        }

        // 현재 걸음수가 목표 걸음수에 도달했으면 끄기 버튼 활성화
        if (mSteps >= setStepNum) {
            stepCountButton.isEnabled = true
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}