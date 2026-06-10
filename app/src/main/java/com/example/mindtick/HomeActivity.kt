package com.example.mindtick

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale
import com.google.android.material.progressindicator.CircularProgressIndicator

class HomeActivity : AppCompatActivity() {

    private var currentNoise = ""
    private var currentMovement = ""
    private var currentTime = ""
    private var currentScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RecordStorage.load(this)

        setContentView(R.layout.activity_home)

        val tvScore =
            findViewById<TextView>(R.id.tvScore)

        val tvNoise =
            findViewById<TextView>(R.id.tvNoise)

        val tvMovement =
            findViewById<TextView>(R.id.tvMovement)

        val tvTime =
            findViewById<TextView>(R.id.tvTime)

        val tvFocusState =
            findViewById<TextView>(R.id.tvFocusState)

        val progressScore =
            findViewById<CircularProgressIndicator>(
                R.id.progressScore
            )

        val btnAnalyzeAgain =
            findViewById<Button>(R.id.btnAnalyzeAgain)

        val navFeedback =
            findViewById<LinearLayout>(R.id.navFeedback)

        val navRecord =
            findViewById<LinearLayout>(R.id.navRecord)

        btnAnalyzeAgain.setOnClickListener {

            val noiseList =
                listOf("낮음", "보통", "높음")

            val movementList =
                listOf("없음", "있음")

            val noise =
                noiseList.random()

            val movement =
                movementList.random()

            val hour =
                Calendar.getInstance()
                    .get(Calendar.HOUR_OF_DAY)

            val time = when {

                hour in 9..18 -> "적합"

                hour in 19..22 -> "보통"

                else -> "부적합"
            }

            tvNoise.text = noise
            tvMovement.text = movement
            tvTime.text = time

            var score = 100

            when (noise) {

                "보통" -> score -= 15

                "높음" -> score -= 30
            }

            if (movement == "있음") {

                score -= 20
            }

            when (time) {

                "보통" -> score -= 10

                "부적합" -> score -= 25
            }

            if (score < 0) {

                score = 0
            }

            tvScore.text =
                score.toString()

            progressScore.progress =
                score

            currentNoise =
                noise

            currentMovement =
                movement

            currentTime =
                time

            currentScore =
                score

            val date =

                SimpleDateFormat(
                    "yyyy-MM-dd HH:mm",
                    Locale.getDefault()
                ).format(
                    System.currentTimeMillis()
                )

            RecordStorage.records.add(

                0,

                RecordItem(

                    date,

                    score,

                    noise,

                    movement,

                    time

                )
            )
            RecordStorage.save(this)

            when {

                score >= 90 -> {

                    tvFocusState.text =
                        "최고의 집중 상태에요!"
                }

                score >= 80 -> {

                    tvFocusState.text =
                        "집중하기 좋은 상태에요!"
                }

                score >= 70 -> {

                    tvFocusState.text =
                        "집중력이 양호해요."
                }

                score >= 60 -> {

                    tvFocusState.text =
                        "집중력이 조금 떨어지고 있어요."
                }

                else -> {

                    tvFocusState.text =
                        "환경 개선이 필요해요!"
                }
            }
        }

        navFeedback.setOnClickListener {

            val intent =
                Intent(
                    this,
                    FeedbackActivity::class.java
                )

            intent.putExtra(
                "noise",
                currentNoise
            )

            intent.putExtra(
                "movement",
                currentMovement
            )

            intent.putExtra(
                "time",
                currentTime
            )

            intent.putExtra(
                "score",
                currentScore
            )

            startActivity(intent)
        }
        navRecord.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RecordActivity::class.java
                )
            )

        }

        // 앱 시작 시 자동 분석
        btnAnalyzeAgain.performClick()

    }
}