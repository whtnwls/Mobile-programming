package com.example.mindtick

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val btnStart = findViewById<Button>(R.id.btnStart)

        btnStart.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    PermissionActivity::class.java
                )
            )

        }
    }
}