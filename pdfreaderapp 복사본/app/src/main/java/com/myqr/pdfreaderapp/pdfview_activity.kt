package com.myqr.pdfreaderapp

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import org.w3c.dom.Text
import java.io.File

class pdfview_activity : AppCompatActivity() {
    private lateinit var pdfView: PDFView
    private lateinit var txView: TextView
    private lateinit var txViewtitle: TextView
    private lateinit var button: Button
    private var pageNumber = 0
    private var retryCount = 0
    private val MAX_RETRY = 5

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfview)
        pdfView = findViewById(R.id.pdfView)
        txView = findViewById(R.id.txtView)
        txViewtitle = findViewById(R.id.pdftx)

        button = findViewById(R.id.button)

        var selectedFileUri=intent.getStringExtra("selectedFileUri")
        var fileName=intent.getStringExtra("fileName")
        var str=selectedFileUri.toString()
        txViewtitle.text="$fileName"

        val contentUri = Uri.parse(str)
        pdfView.fromUri(contentUri)
            .onPageChange { page, pageCount ->
                var current_page=page+1
                txView.text="$current_page"
            }
            .load()
        button?.setOnClickListener {
            finish()
            openSystemPicker()
        }
    }
    private fun showPDF(uri:String) {
        Toast.makeText(this,"$uri",Toast.LENGTH_SHORT).show()
        //Log.d("ViewPDFAct", "FilePath: ${intent.getStringExtra("pdfFile")}")
        pdfView.fromFile(File(uri))
            .defaultPage(pageNumber)
            //.onPageChange(this)
            //.onLoad(this)
            .enableAnnotationRendering(true)
            .onError(errorListener)
            .scrollHandle(DefaultScrollHandle(this))
            .load()
    }
    private val errorListener = OnErrorListener { t ->
        t.printStackTrace()
        if (retryCount >= MAX_RETRY) {
            //Toast.makeText(this@ViewPDFAct, "PDF error", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            retryCount++
            Handler().postDelayed({
                //showPDF()
            }, 1000)
        }
    }

    private val REQUEST_CODE_PICK_FILE = 1
    private fun openSystemPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "*/*" // 모든 파일 타입
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE)
    }
    override fun onBackPressed() {
        super.onBackPressed() // 기본 뒤로가기 동작 수행
        finish()
        openSystemPicker()
    }

}