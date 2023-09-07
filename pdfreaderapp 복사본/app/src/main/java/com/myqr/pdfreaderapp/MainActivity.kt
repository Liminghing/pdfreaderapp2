package com.myqr.pdfreaderapp


import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_PICK_FILE = 1
    var imageView:ImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView=findViewById(R.id.imageView)
        imageView?.setOnClickListener {
            openSystemPicker()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val writePermission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            val readPermission = android.Manifest.permission.READ_EXTERNAL_STORAGE

            if (checkSelfPermission(writePermission) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(readPermission) != PackageManager.PERMISSION_GRANTED
            ) {
                if (shouldShowRequestPermissionRationale(writePermission)) {
                    //Toast.makeText(this, "외부 저장소 사용을 위해 읽기/쓰기 필요", Toast.LENGTH_SHORT).show()
                }

                requestPermissions(
                    arrayOf(writePermission, readPermission),
                    2
                )
            }
            else{
                //finish()
                openSystemPicker()

            }
        }



    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2) { // 요청한 퍼미션 요청 코드 (여기서는 2)와 일치해야 함
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this,"permissin is ready",Toast.LENGTH_SHORT).show()
                openSystemPicker()
            } else {
                // 퍼미션이 거부되었을 때 수행할 동작
            }
        }
    }
    fun getMimeType(contentResolver: ContentResolver, uri: Uri): String? {
        return contentResolver.getType(uri)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { selectedFileUri ->
                // 선택한 파일의 URI를 사용하여 필요한 작업 수행
                // 예: 파일 읽기, 업로드 등
                val mimeType = getMimeType(contentResolver, selectedFileUri)

                if(mimeType=="application/pdf"){
                    val fileName = getFileNameFromUri(selectedFileUri)
                    finish()
                    val intent = Intent(this, pdfview_activity::class.java)
                    var str=selectedFileUri.toString()
                    intent.putExtra("selectedFileUri", str)
                    intent.putExtra("fileName", fileName)
                    startActivity(intent)
                }else{
                    Toast.makeText(this,"This is not a pdf file",Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
    //파일 명 추춮
    private fun getFileNameFromUri(uri: Uri): String? {
        var fileName: String? = null
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            fileName = cursor.getString(nameIndex)
        }
        return fileName
    }
    // 파일 선택기 열기
    private fun openSystemPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "*/*" // 모든 파일 타입
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE)
    }


}