package com.example.mindtick

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class PermissionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        val btnBack =
            findViewById<ImageButton>(R.id.btnBack)

        val btnAllowAll =
            findViewById<AppCompatButton>(R.id.btnAllowAll)

        val btnLater =
            findViewById<AppCompatButton>(R.id.btnLater)

        // 뒤로가기
        btnBack.setOnClickListener {
            finish()
        }

        // 모두 허용하기
        btnAllowAll.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    HomeActivity::class.java
                )
            )

            finish()
        }

        // 나중에 하기
        btnLater.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    HomeActivity::class.java
                )
            )

            finish()
        }
    }
}