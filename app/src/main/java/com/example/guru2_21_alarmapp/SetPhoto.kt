package com.example.guru2_21_alarmapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class SetPhoto : AppCompatActivity() {

    private val REQUEST_READ_EXTERNAL_STORAGE = 1000

    lateinit var camera: Button
    lateinit var gallery: Button
    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_photo)

        //camera = findViewById(R.id.camera)
        gallery = findViewById(R.id.sel_galleryBtn)
        imageView = findViewById(R.id.sel_imageView)


//        camera.setOnClickListener {
//            val intent = Intent(this@SetPhoto, CameraTake::class.java)
//            startActivity(intent)
//        }

        gallery.setOnClickListener {
            checkStoragePermission()
        }

    }

    private fun checkStoragePermission() {
        val readImagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES
        else Manifest.permission.READ_EXTERNAL_STORAGE //버전 확인

        //권한 부여 여부 확인
        if (ContextCompat.checkSelfPermission(this,
                readImagePermission) != PackageManager.PERMISSION_GRANTED) {

            //권한이 허용되지 않았을 때
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    readImagePermission
                )
            ) {
                //이전에 권한이 거부된 적이 있을 때
                var dlg = AlertDialog.Builder(this)
                dlg.setTitle("권한이 필요한 이유")
                dlg.setMessage("사진 정보를 얻기 위해서는 외부 저장소 권한이 필수로 필요합니다.") //권한이 필요한 이유 설명
                dlg.setPositiveButton("확인") { dialog, which ->
                    ActivityCompat.requestPermissions(
                        this@SetPhoto,
                        arrayOf(readImagePermission),
                        REQUEST_READ_EXTERNAL_STORAGE //권한 재요청
                    )
                }
                dlg.setNegativeButton("취소", null)
                dlg.show()
            }
            //권한이 거부된 적이 없을 때 (권한을 요청한 적이 없을 때)
            else {
                //권한 요청
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(readImagePermission),
                    REQUEST_READ_EXTERNAL_STORAGE
                )
            }
        }
        //권한이 이미 허용되었을 때
        else {
            openGallery()
        }
    }

    private fun openGallery() {
        // 갤러리를 여는 Intent 생성
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_READ_EXTERNAL_STORAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE && resultCode == RESULT_OK && data != null) {
            // 갤러리에서 사진을 선택한 경우
            val selectedImageUri: Uri? = data.data

            //예외처리, savePhoto() 실행
            if (selectedImageUri != null) {
                savePhoto(selectedImageUri)
            }
        }
    }

    //사진을 앱 내부에 저장
    private fun savePhoto(selectedImageUri: Uri) {
        try {
            //선택한 이미지의 InputStream 가져오기
            val inputStream: InputStream? = contentResolver.openInputStream(selectedImageUri)

            //앱 내부에 이미지를 저장할 파일 경로 생성
            val internalStoragePath = getInternalStoragePath()
            val imageFile = File(internalStoragePath, "selected_image.jpg")

            //파일 출력 스트림을 생성하여 이미지를 저장
            val outputStream = FileOutputStream(imageFile)
            inputStream?.copyTo(outputStream)

            //파일 닫기
            outputStream.close()
            inputStream?.close()

            //저장된 이미지 출력
            viewPhoto(Uri.fromFile(imageFile))
            //Log.i("MainActivity", "ViewPhoto 실행")

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // 앱 내부 저장소 경로를 가져오기
    private fun getInternalStoragePath(): String {
        return filesDir.absolutePath
    }

    //imageView에 선택한 이미지 출력
    private fun viewPhoto(selectedImageUri: Uri?) {
        //Log.i("MainActivity", "ViewPhoto 실행2")
        selectedImageUri?.let {
            try {
                // 선택한 이미지의 Uri를 이용하여 이미지를 가져오는 로직을 구현
                val inputStream: InputStream? = contentResolver.openInputStream(it)

                // BitmapFactory를 사용하여 InputStream에서 Bitmap으로 변환
                val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)

                // ImageView에 Bitmap 설정
                imageView.setImageBitmap(bitmap)

                // 필요한 경우, 이미지를 화면에 맞게 조절하는 등의 로직 추가 가능
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}