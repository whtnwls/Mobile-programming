package com.example.mindtick

import android.widget.ImageButton
import java.util.Calendar
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecordActivity : AppCompatActivity() {

    private var currentMode = "DAY"

    private var currentDate =
        Date()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        val tvDate =
            findViewById<TextView>(
                R.id.tvDate
            )

        fun updateDate() {

            tvDate.text =
                SimpleDateFormat(
                    "yyyy년 MM월 dd일 (E)",
                    Locale.KOREA
                ).format(
                    currentDate
                )
        }

        updateDate()

        val recyclerHistory =
            findViewById<RecyclerView>(
                R.id.recyclerHistory
            )
        val btnPrev =
            findViewById<ImageButton>(
                R.id.btnPrev
            )

        val btnNext =
            findViewById<ImageButton>(
                R.id.btnNext
            )

        val tvAvgScore =
            findViewById<TextView>(
                R.id.tvAvgScore
            )

        val tvLevel =
            findViewById<TextView>(
                R.id.tvLevel
            )

        val tvAnalysisTime =
            findViewById<TextView>(
                R.id.tvAnalysisTime
            )

        val lineChart =
            findViewById<LineChart>(
                R.id.lineChart
            )

        val btnDay =
            findViewById<Button>(
                R.id.btnDay
            )

        val btnWeek =
            findViewById<Button>(
                R.id.btnWeek
            )

        val btnMonth =
            findViewById<Button>(
                R.id.btnMonth
            )
        btnDay.setOnClickListener {

            currentMode = "DAY"

            btnDay.setBackgroundResource(
                R.drawable.btn_filled_green
            )

            btnWeek.setBackgroundResource(
                R.drawable.btn_outline_green
            )

            btnMonth.setBackgroundResource(
                R.drawable.btn_outline_green
            )

            btnDay.setTextColor(
                getColor(android.R.color.white)
            )

            btnWeek.setTextColor(
                getColor(android.R.color.darker_gray)
            )

            btnMonth.setTextColor(
                getColor(android.R.color.darker_gray)
            )

            tvDate.text =
                SimpleDateFormat(
                    "yyyy년 MM월 dd일",
                    Locale.KOREA
                ).format(
                    Date()
                )

            recyclerHistory.adapter =
                RecordAdapter(
                    RecordStorage.records
                )
        }

        btnWeek.setOnClickListener {

            currentMode = "WEEK"

            btnDay.setBackgroundResource(
                R.drawable.btn_outline_green
            )

            btnWeek.setBackgroundResource(
                R.drawable.btn_filled_green
            )

            btnMonth.setBackgroundResource(
                R.drawable.btn_outline_green
            )

            btnDay.setTextColor(
                getColor(android.R.color.darker_gray)
            )

            btnWeek.setTextColor(
                getColor(android.R.color.white)
            )

            btnMonth.setTextColor(
                getColor(android.R.color.darker_gray)
            )

            tvDate.text =
                "최근 7일"

            recyclerHistory.adapter =
                RecordAdapter(
                    RecordStorage.records.take(7)
                )
        }

        btnMonth.setOnClickListener {

            currentMode = "MONTH"

            btnDay.setBackgroundResource(
                R.drawable.btn_outline_green
            )

            btnWeek.setBackgroundResource(
                R.drawable.btn_outline_green
            )

            btnMonth.setBackgroundResource(
                R.drawable.btn_filled_green
            )

            btnDay.setTextColor(
                getColor(android.R.color.darker_gray)
            )

            btnWeek.setTextColor(
                getColor(android.R.color.darker_gray)
            )

            btnMonth.setTextColor(
                getColor(android.R.color.white)
            )

            tvDate.text =
                "최근 30일"

            recyclerHistory.adapter =
                RecordAdapter(
                    RecordStorage.records.take(30)
                )
        }

        btnPrev.setOnClickListener {

            val cal =
                Calendar.getInstance()

            cal.time =
                currentDate

            cal.add(
                Calendar.DAY_OF_MONTH,
                -1
            )

            currentDate =
                cal.time

            updateDate()
        }

        btnNext.setOnClickListener {

            val cal =
                Calendar.getInstance()

            cal.time =
                currentDate

            cal.add(
                Calendar.DAY_OF_MONTH,
                1
            )

            currentDate =
                cal.time

            updateDate()
        }

        if (RecordStorage.records.isNotEmpty()) {

            val avg =
                RecordStorage.records
                    .map { it.score }
                    .average()
                    .toInt()

            tvAvgScore.text =
                avg.toString()

            when {

                avg >= 90 -> tvLevel.text = "최고"

                avg >= 80 -> tvLevel.text = "좋음"

                avg >= 70 -> tvLevel.text = "양호"

                avg >= 60 -> tvLevel.text = "보통"

                else -> tvLevel.text = "낮음"
            }

            val count =
                RecordStorage.records.size

            val totalMinutes =
                count * 10

            val hour =
                totalMinutes / 60

            val minute =
                totalMinutes % 60

            tvAnalysisTime.text =
                "분석 시간 ${hour}시간 ${minute}분"
            // 그래프 데이터 생성

            val entries =
                ArrayList<Entry>()

            RecordStorage.records
                .reversed()
                .forEachIndexed { index, record ->

                    entries.add(
                        Entry(
                            index.toFloat(),
                            record.score.toFloat()
                        )
                    )
                }

            val dataSet =
                LineDataSet(
                    entries,
                    "집중 점수"
                )

            dataSet.lineWidth = 2.5f

            dataSet.circleRadius = 4f

            dataSet.setDrawValues(false)

            dataSet.color =
                getColor(R.color.primary)

            dataSet.setCircleColor(
                getColor(R.color.primary)
            )

            val lineData =
                LineData(dataSet)

            lineChart.data =
                lineData

            lineChart.description.isEnabled =
                false

            lineChart.legend.isEnabled =
                false

            lineChart.axisRight.isEnabled =
                false

            lineChart.xAxis.setDrawGridLines(
                false
            )

            lineChart.axisLeft.axisMinimum =
                0f

            lineChart.axisLeft.axisMaximum =
                100f

            lineChart.invalidate()

        } else {

            tvAvgScore.text =
                "0"

            tvLevel.text =
                "없음"

            tvAnalysisTime.text =
                "분석 기록 없음"

            lineChart.clear()
        }

        recyclerHistory.layoutManager =
            LinearLayoutManager(this)

        recyclerHistory.adapter =
            RecordAdapter(
                RecordStorage.records
            )

        btnDay.performClick()

        val navHome =
            findViewById<LinearLayout>(
                R.id.navHome
            )

        val navFeedback =
            findViewById<LinearLayout>(
                R.id.navFeedback
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