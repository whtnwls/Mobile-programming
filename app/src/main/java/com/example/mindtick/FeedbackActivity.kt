package com.example.mindtick

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class FeedbackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val tvNoiseTitle =
            findViewById<TextView>(
                R.id.tvNoiseTitle
            )

        val tvNoiseDesc =
            findViewById<TextView>(
                R.id.tvNoiseDesc
            )

        val tvMovementTitle =
            findViewById<TextView>(
                R.id.tvMovementTitle
            )

        val tvMovementDesc =
            findViewById<TextView>(
                R.id.tvMovementDesc
            )

        val tvTimeTitle =
            findViewById<TextView>(
                R.id.tvTimeTitle
            )

        val tvTimeDesc =
            findViewById<TextView>(
                R.id.tvTimeDesc
            )

        val btnNewFeedback =
            findViewById<Button>(
                R.id.btnNewFeedback
            )
        val noise =
            intent.getStringExtra(
                "noise"
            ) ?: ""

        val movement =
            intent.getStringExtra(
                "movement"
            ) ?: ""

        val time =
            intent.getStringExtra(
                "time"
            ) ?: ""

        val score =
            intent.getIntExtra(
                "score",
                0
            )

        fun loadFeedback() {

            lifecycleScope.launch {

                tvNoiseDesc.text =
                    "AI 분석 중..."

                tvMovementDesc.text =
                    "AI 분석 중..."

                tvTimeDesc.text =
                    "AI 분석 중..."

                // 소음 AI

                val noisePrompt =
                    """
                    현재 소음 상태는 $noise 입니다.

                    공부 집중력 향상을 위한
                    짧은 조언을 한국어 한 문장으로 작성해.
                    30자 이내.
                    매번 다른 표현으로 작성해.
                    """

                val noiseResult =
                    GroqManager.getFeedback(
                        noisePrompt
                    )
                // 움직임 AI

                val movementPrompt =
                    """
                    현재 움직임 상태는 $movement 입니다.

                    공부 집중력 향상을 위한
                    짧은 조언을 한국어 한 문장으로 작성해.
                    30자 이내.
                    매번 다른 표현으로 작성해.
                    """

                val movementResult =
                    GroqManager.getFeedback(
                        movementPrompt
                    )

                // 시간대 AI

                val timePrompt =
                    """
                    현재 시간대 상태는 $time 입니다.

                    공부 집중력 향상을 위한
                    짧은 조언을 한국어 한 문장으로 작성해.
                    30자 이내.
                    매번 다른 표현으로 작성해.
                    """

                val timeResult =
                    GroqManager.getFeedback(
                        timePrompt
                    )

                when (noise) {

                    "높음" ->
                        tvNoiseTitle.text =
                            "주변 소음이 높아요"

                    "보통" ->
                        tvNoiseTitle.text =
                            "소음이 조금 있어요"

                    else ->
                        tvNoiseTitle.text =
                            "집중하기 좋은 환경이에요"
                }

                tvMovementTitle.text =
                    if (movement == "있음")
                        "이동 중으로 감지돼요"
                    else
                        "안정적인 상태예요"
                tvTimeTitle.text =
                    when (time) {

                        "부적합" ->
                            "늦은 시간대예요"

                        "보통" ->
                            "집중력이 떨어질 수 있어요"

                        else ->
                            "학습하기 좋은 시간이에요"
                    }

                tvNoiseDesc.text =
                    noiseResult

                tvMovementDesc.text =
                    movementResult

                tvTimeDesc.text =
                    timeResult

                if (score < 60) {

                    tvTimeDesc.text =
                        tvTimeDesc.text.toString() +
                                "\n\n현재 집중 점수가 낮아 환경 개선이 필요합니다."
                }
            }
        }

        // 처음 실행
        loadFeedback()

        // 새 피드백 받기
        btnNewFeedback.setOnClickListener {

            loadFeedback()
        }

        val navHome =
            findViewById<LinearLayout>(
                R.id.navHome
            )

        val navRecord =
            findViewById<LinearLayout>(
                R.id.navRecord
            )

        navHome.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    HomeActivity::class.java
                )
            )

            finish()
        }

        navRecord.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RecordActivity::class.java
                )
            )

            finish()
        }
    }
}