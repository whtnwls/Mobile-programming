package com.example.mindtick

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FeedbackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val tvNoiseTitle =
            findViewById<TextView>(R.id.tvNoiseTitle)

        val tvNoiseDesc =
            findViewById<TextView>(R.id.tvNoiseDesc)

        val tvMovementTitle =
            findViewById<TextView>(R.id.tvMovementTitle)

        val tvMovementDesc =
            findViewById<TextView>(R.id.tvMovementDesc)

        val tvTimeTitle =
            findViewById<TextView>(R.id.tvTimeTitle)

        val tvTimeDesc =
            findViewById<TextView>(R.id.tvTimeDesc)

        val noise =
            intent.getStringExtra("noise") ?: ""

        val movement =
            intent.getStringExtra("movement") ?: ""

        val time =
            intent.getStringExtra("time") ?: ""

        val score =
            intent.getIntExtra("score", 0)

        // 소음 피드백

        when (noise) {

            "높음" -> {

                tvNoiseTitle.text =
                    "주변 소음이 높아요"

                tvNoiseDesc.text =
                    "이어폰 사용 또는 조용한 장소 이동을 추천합니다."
            }

            "보통" -> {

                tvNoiseTitle.text =
                    "소음이 조금 있어요"

                tvNoiseDesc.text =
                    "집중을 위해 주변 환경을 정리해보세요."
            }

            else -> {

                tvNoiseTitle.text =
                    "집중하기 좋은 환경이에요"

                tvNoiseDesc.text =
                    "현재 주변 소음이 낮아 집중하기 좋습니다."
            }
        }

        // 움직임 피드백

        when (movement) {

            "있음" -> {

                tvMovementTitle.text =
                    "이동 중으로 감지돼요"

                tvMovementDesc.text =
                    "암기 위주의 학습이 효과적입니다."
            }

            else -> {

                tvMovementTitle.text =
                    "안정적인 상태예요"

                tvMovementDesc.text =
                    "깊은 집중이 가능한 환경입니다."
            }
        }

        // 시간대 피드백

        when (time) {

            "부적합" -> {

                tvTimeTitle.text =
                    "늦은 시간대예요"

                tvTimeDesc.text =
                    "짧은 휴식 후 학습을 권장합니다."
            }

            "보통" -> {

                tvTimeTitle.text =
                    "집중력이 떨어질 수 있어요"

                tvTimeDesc.text =
                    "가벼운 스트레칭을 추천합니다."
            }

            else -> {

                tvTimeTitle.text =
                    "학습하기 좋은 시간이에요"

                tvTimeDesc.text =
                    "현재 집중력이 높게 유지될 가능성이 있습니다."
            }
        }

        // 점수가 낮으면 추가 경고

        if (score < 60) {

            tvTimeDesc.text =
                tvTimeDesc.text.toString() +
                        "\n\n현재 집중 점수가 낮아 환경 개선이 필요합니다."
        }

        val navHome =
            findViewById<LinearLayout>(R.id.navHome)

        val navRecord =
            findViewById<LinearLayout>(R.id.navRecord)

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