package com.myqr.pdfreaderapp

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast

class PdfImportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_import)
        // "내 파일" 앱으로부터 전달받은 Intent
        val receivedIntent = intent

        // 전달받은 Intent에서 데이터 추출
        val data: Uri? = receivedIntent.data

        // 데이터가 유효한 경우
        if (data != null) {
            val mimeType = getMimeType(contentResolver, data)

            if(mimeType=="application/pdf"){
                // 여러분의 어플리케이션의 PDFViewerActivity를 실행하는 Intent 생성
                val pdfViewerIntent = Intent(this, pdfintentactivity::class.java)
                val fileName = getFileNameFromUri(data)
                pdfViewerIntent.putExtra("pdfUri", data.toString())
                pdfViewerIntent.putExtra("fileName",fileName)
                startActivity(pdfViewerIntent)
            }else{
                Toast.makeText(this,"This is not a pdf file",Toast.LENGTH_SHORT).show()
            }
        } else {
            // 데이터가 없는 경우 처리
        }
    }
    fun getMimeType(contentResolver: ContentResolver, uri: Uri): String? {
        return contentResolver.getType(uri)
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


}