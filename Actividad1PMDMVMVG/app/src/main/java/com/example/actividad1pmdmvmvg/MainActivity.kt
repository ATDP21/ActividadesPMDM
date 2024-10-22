package com.example.actividad1pmdmvmvg

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextUrl = findViewById<EditText>(R.id.editTextURL)
        val buttonValidate = findViewById<Button>(R.id.buttonValidate)

        buttonValidate.setOnClickListener {
            val urlText = editTextUrl.text.toString()


            if (urlText.isEmpty()) {
                Toast.makeText(this, "Por favor, introduce una URL", Toast.LENGTH_SHORT).show()
            } else if (!isValidUrl(urlText)) {
                Toast.makeText(this, "Introduce una URL válida", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlText))
                startActivity(intent)
            }
        }
    }


    private fun isValidUrl(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }
}
