package com.example.guru2_21_alarmapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class TakePhoto: AppCompatActivity() {

    lateinit var cameraOn: Button
    lateinit var imageView: ImageView

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_photo)

        cameraOn = findViewById(R.id.tak_cameraOn)
        imageView = findViewById(R.id.tak_imageView)

        //버튼 클릭 시 함수 실행
        cameraOn.setOnClickListener {
            takePicture()
        }
    }

    private fun takePicture() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureLauncher.launch(takePictureIntent)
    }
}