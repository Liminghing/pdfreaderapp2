package com.myqr.pdfreaderapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.github.barteksc.pdfviewer.PDFView
import java.io.File

class pdfintentactivity : AppCompatActivity() {
    var pdfview: PDFView?=null
    var txv:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfintentactivity)
        pdfview=findViewById(R.id.PdfViewTntent)
        txv=findViewById(R.id.pdftx_intent)
        // 전달받은 PDF 파일의 URI

        val pdfUriString = intent.getStringExtra("pdfUri")
        var fileName=intent.getStringExtra("fileName")
        txv?.text="$fileName"
        val pdfUri = Uri.parse(pdfUriString)
        pdfview?.fromUri(pdfUri)
            ?.load()
    }

    override fun onBackPressed() {
        super.onBackPressed() // 기본 뒤로가기 동작 수행
        finish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}