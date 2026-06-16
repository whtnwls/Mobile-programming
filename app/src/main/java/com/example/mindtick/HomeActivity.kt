package com.example.mindtick

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.progressindicator.CircularProgressIndicator
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.sqrt

class HomeActivity :
    AppCompatActivity(),
    SensorEventListener {

    private var currentNoise = ""
    private var currentMovement = ""
    private var currentTime = ""
    private var currentScore = 0

    private var sensorManager: SensorManager? = null

    private var movementDetected = false

    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f

    private fun getNoiseLevel(): String {

        if (
            checkSelfPermission(
                android.Manifest.permission.RECORD_AUDIO
            ) !=
            android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {

            return "권한없음"
        }

        return try {

            val sampleRate = 44100

            val bufferSize =
                AudioRecord.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )

            val audioRecord =
                AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    sampleRate,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize
                )

            val buffer =
                ShortArray(bufferSize)

            audioRecord.startRecording()

            Thread.sleep(1000)

            audioRecord.read(
                buffer,
                0,
                buffer.size
            )

            audioRecord.stop()
            audioRecord.release()

            var sum = 0.0

            for (sample in buffer) {
                sum += sample * sample
            }

            val rms =
                sqrt(sum / buffer.size)

            when {
                rms < 300 -> "낮음"
                rms < 1500 -> "보통"
                else -> "높음"
            }

        } catch (e: Exception) {

            e.printStackTrace()
            "보통"
        }
    }
    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        RecordStorage.load(this)

        setContentView(
            R.layout.activity_home
        )

        sensorManager =
            getSystemService(
                SENSOR_SERVICE
            ) as SensorManager

        val accelerometer =
            sensorManager?.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER
            )

        sensorManager?.registerListener(
            this,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        val tvScore =
            findViewById<TextView>(
                R.id.tvScore
            )

        val tvNoise =
            findViewById<TextView>(
                R.id.tvNoise
            )

        val tvMovement =
            findViewById<TextView>(
                R.id.tvMovement
            )

        val tvTime =
            findViewById<TextView>(
                R.id.tvTime
            )

        val tvFocusState =
            findViewById<TextView>(
                R.id.tvFocusState
            )

        val progressScore =
            findViewById<CircularProgressIndicator>(
                R.id.progressScore
            )

        val btnAnalyzeAgain =
            findViewById<Button>(
                R.id.btnAnalyzeAgain
            )

        val navFeedback =
            findViewById<LinearLayout>(
                R.id.navFeedback
            )

        val navRecord =
            findViewById<LinearLayout>(
                R.id.navRecord
            )

        tvScore.text = "--"

        tvNoise.text = "--"

        tvMovement.text = "--"

        tvTime.text = "--"

        tvFocusState.text =
            "분석 전입니다."

        progressScore.progress = 0

        btnAnalyzeAgain.setOnClickListener {

            tvFocusState.text =
                "분석 중입니다..."

            btnAnalyzeAgain.isEnabled = false

            tvFocusState.postDelayed({

                val noise =
                    getNoiseLevel()

                val movement =
                    if (movementDetected)
                        "있음"
                    else
                        "없음"

                val hour =
                    Calendar.getInstance()
                        .get(Calendar.HOUR_OF_DAY)

                val time = when {

                    hour in 9..18 -> "적합"

                    hour in 19..22 -> "보통"

                    else -> "부적합"
                }

                tvNoise.text =
                    noise

                tvMovement.text =
                    movement

                tvTime.text =
                    time

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

                btnAnalyzeAgain.isEnabled =
                    true

                movementDetected = false

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

            }, 2000)
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

        val skipAnalysis =
            intent.getBooleanExtra(
                "skipAnalysis",
                false
            )

        if (!skipAnalysis) {

            btnAnalyzeAgain.performClick()
        }
    }

    override fun onSensorChanged(
        event: SensorEvent?
    ) {

        event ?: return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val delta =
            kotlin.math.abs(x - lastX) +
                    kotlin.math.abs(y - lastY) +
                    kotlin.math.abs(z - lastZ)

        if (delta > 5) {

            movementDetected = true
        }

        lastX = x
        lastY = y
        lastZ = z
    }

    override fun onAccuracyChanged(
        sensor: Sensor?,
        accuracy: Int
    ) {
    }

    override fun onDestroy() {

        super.onDestroy()

        sensorManager?.unregisterListener(
            this
        )
    }
}
