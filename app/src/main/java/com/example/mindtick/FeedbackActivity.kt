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
                    현재 사용자의 주변 소음 상태는 "$noise" 입니다.
                
                    당신은 집중력 향상을 돕는 학습 코치입니다.
                
                    규칙:
                    - 한국어로 작성
                    - 존댓말 사용
                    - 3문장으로 작성
                    - 50자 이내
                    - 사용자의 집중력 향상에 도움이 되는 실질적인 조언 제공
                    - 매번 다른 표현 사용
                    - 역할극, 감정 표현, 잡담 금지
                    - 반드시 조언 형태로 작성
                
                     예시:

                    낮음:
                    주변이 조용해 집중하기 좋은 환경입니다.
                    현재 상태를 유지해 보세요.
                
                    보통:
                    약간의 소음이 있을 수 있습니다.
                    알림을 끄고 학습에 집중해 보세요.
                
                    높음:
                    주변 소음이 큰 편입니다.
                    조용한 장소나 이어폰 사용을 추천드립니다.
                
                    반드시 사용자에게 도움이 되는 학습 조언만 출력하세요.
                
                    조언:
                    """.trimIndent()

                val noiseResult =
                    GroqManager.getFeedback(
                        noisePrompt
                    )
                // 움직임 AI

                val movementPrompt =
                    """
                    현재 사용자의 움직임 상태는 "$movement" 입니다.

                    당신은 학습 집중 코치입니다.

                    규칙:
                    - 한국어로 작성
                    - 존댓말 사용
                    - 3문장으로 작성
                    - 50자 이내
                    - 사용자의 집중력 향상에 도움이 되는 실질적인 조언 제공
                    - 매번 다른 표현 사용
                    - 역할극, 감정 표현, 잡담 금지
                    - 반드시 조언 형태로 작성
                
                    예시:

                    없음:
                    현재 안정적인 상태로 집중하기 좋습니다.
                    중요한 학습을 진행해 보세요.
                
                    있음:
                    이동 중이라면 암기 위주 학습을 추천드립니다.
                    안정된 장소에서 공부하면 더욱 효과적입니다.
                
                    반드시 사용자에게 도움이 되는 학습 조언만 출력하세요.
                
                    조언:
                    """.trimIndent()

                val movementResult =
                    GroqManager.getFeedback(
                        movementPrompt
                    )

                // 시간대 AI

                val timePrompt =
                    """
                    현재 사용자의 시간대 상태는 "$time" 입니다.
                
                    당신은 집중력 향상을 돕는 학습 코치입니다.
                
                    규칙:
                    - 한국어로 작성
                    - 존댓말 사용
                    - 3문단으로 작성
                    - 50자 이내
                    - 사용자의 집중력 향상에 도움이 되는 실질적인 조언 제공
                    - 매번 다른 표현 사용
                    - 역할극, 감정 표현, 잡담 금지
                    - 반드시 조언 형태로 작성
                
                    예시:

                    적합:
                    현재는 집중하기 좋은 시간대입니다.
                    중요한 학습을 진행해 보세요.
                    
                    보통:
                    집중력이 다소 떨어질 수 있는 시간대입니다.
                    짧은 휴식을 병행해 보세요.
                    
                    부적합:
                    늦은 시간대에는 피로가 쌓일 수 있습니다.
                    충분한 휴식 후 학습을 권장드립니다.
                
                    조언:
                    """.trimIndent()

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