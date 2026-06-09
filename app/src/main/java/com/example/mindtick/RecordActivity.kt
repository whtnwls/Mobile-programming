package com.example.mindtick

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class RecordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        val navHome =
            findViewById<LinearLayout>(R.id.navHome)

        val navFeedback =
            findViewById<LinearLayout>(R.id.navFeedback)

        navHome.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    HomeActivity::class.java
                )
            )

            finish()
        }

        navFeedback.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    FeedbackActivity::class.java
                )
            )

            finish()
        }
    }
}