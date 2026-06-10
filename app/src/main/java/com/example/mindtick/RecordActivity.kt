package com.example.mindtick

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
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
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RecordActivity : AppCompatActivity() {

    private var currentMode = "DAY"

    private var currentDate =
        Date()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        RecordStorage.load(this)

        setContentView(
            R.layout.activity_record
        )

        val tvDate =
            findViewById<TextView>(
                R.id.tvDate
            )

        val recyclerHistory =
            findViewById<RecyclerView>(
                R.id.recyclerHistory
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

        val btnPrev =
            findViewById<ImageButton>(
                R.id.btnPrev
            )

        val btnNext =
            findViewById<ImageButton>(
                R.id.btnNext
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

        fun updateDateText() {

            tvDate.text =
                SimpleDateFormat(
                    "yyyy년 MM월 dd일 (E)",
                    Locale.KOREA
                ).format(
                    currentDate
                )
        }

        fun getFilteredRecords():
                List<RecordItem> {

            val format =
                SimpleDateFormat(
                    "yyyy-MM-dd HH:mm",
                    Locale.getDefault()
                )

            return when (
                currentMode
            ) {

                "DAY" -> {

                    val selectedDate =
                        SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        ).format(
                            currentDate
                        )

                    RecordStorage.records.filter {

                        it.date.startsWith(
                            selectedDate
                        )
                    }
                }
                "WEEK" -> {

                    val cal =
                        Calendar.getInstance()

                    cal.time =
                        currentDate

                    val endDate =
                        cal.timeInMillis

                    cal.add(
                        Calendar.DAY_OF_YEAR,
                        -6
                    )

                    val startDate =
                        cal.timeInMillis

                    RecordStorage.records.filter {

                        val recordTime =
                            format.parse(
                                it.date
                            )?.time ?: 0L

                        recordTime in
                                startDate..endDate
                    }
                }

                else -> {

                    val cal =
                        Calendar.getInstance()

                    cal.time =
                        currentDate

                    val endDate =
                        cal.timeInMillis

                    cal.add(
                        Calendar.DAY_OF_YEAR,
                        -29
                    )

                    val startDate =
                        cal.timeInMillis

                    RecordStorage.records.filter {

                        val recordTime =
                            format.parse(
                                it.date
                            )?.time ?: 0L

                        recordTime in
                                startDate..endDate
                    }
                }
            }
        }

        fun refreshUI() {

            val records =
                getFilteredRecords()

            recyclerHistory.layoutManager =
                LinearLayoutManager(
                    this@RecordActivity
                )

            recyclerHistory.adapter =
                RecordAdapter(
                    records
                )

            if (records.isEmpty()) {

                tvAvgScore.text =
                    "0"

                tvLevel.text =
                    "없음"

                tvAnalysisTime.text =
                    "기록 없음"

                lineChart.clear()

                return
            }

            val avg =
                records
                    .map {
                        it.score
                    }
                    .average()
                    .toInt()

            tvAvgScore.text =
                avg.toString()

            when {

                avg >= 90 ->
                    tvLevel.text = "최고"

                avg >= 80 ->
                    tvLevel.text = "좋음"

                avg >= 70 ->
                    tvLevel.text = "양호"

                avg >= 60 ->
                    tvLevel.text = "보통"

                else ->
                    tvLevel.text = "낮음"
            }

            val totalMinutes =
                records.size * 10

            val hour =
                totalMinutes / 60

            val minute =
                totalMinutes % 60

            tvAnalysisTime.text =
                "분석 시간 ${hour}시간 ${minute}분"

            val entries =
                ArrayList<Entry>()
            records
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

            dataSet.lineWidth =
                2.5f

            dataSet.circleRadius =
                4f

            dataSet.setDrawValues(
                false
            )

            dataSet.color =
                getColor(
                    R.color.primary
                )

            dataSet.setCircleColor(
                getColor(
                    R.color.primary
                )
            )

            lineChart.data =
                LineData(
                    dataSet
                )

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
        }

        btnDay.setOnClickListener {

            currentMode =
                "DAY"

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
                getColor(
                    android.R.color.white
                )
            )

            btnWeek.setTextColor(
                getColor(
                    android.R.color.darker_gray
                )
            )

            btnMonth.setTextColor(
                getColor(
                    android.R.color.darker_gray
                )
            )

            updateDateText()

            refreshUI()
        }

        btnWeek.setOnClickListener {

            currentMode =
                "WEEK"

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
                getColor(
                    android.R.color.darker_gray
                )
            )

            btnWeek.setTextColor(
                getColor(
                    android.R.color.white
                )
            )

            btnMonth.setTextColor(
                getColor(
                    android.R.color.darker_gray
                )
            )

            refreshUI()
        }
        btnMonth.setOnClickListener {

            currentMode =
                "MONTH"

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
                getColor(
                    android.R.color.darker_gray
                )
            )

            btnWeek.setTextColor(
                getColor(
                    android.R.color.darker_gray
                )
            )

            btnMonth.setTextColor(
                getColor(
                    android.R.color.white
                )
            )

            refreshUI()
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

            updateDateText()

            refreshUI()
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

            updateDateText()

            refreshUI()
        }

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
