package com.jm.schoolproject.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jm.schoolproject.CalendarData
import com.jm.schoolproject.R
import com.jm.schoolproject.UserData
import com.jm.schoolproject.server.Record
import com.jm.schoolproject.server.Schedule
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class HomeMenuFragment : Fragment(), HomeCalendarAdapter.OnItemListener {
    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var dateTV: TextView
    private lateinit var previousBtn: Button
    private lateinit var nextBtn: Button
    private lateinit var homeTitle : TextView
    private var schedules = ArrayList<Schedule>()
    private var records = ArrayList<Record>()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, null).apply {
            var dateFormat = SimpleDateFormat("yyyy-MM-dd/hh-mm")
            var startTime = Calendar.getInstance().apply {
                time = dateFormat.parse(UserData.getInstance().register_day)
            }.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time.time
            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time.time

            var diff = (today - startTime) / (24 * 60 * 60 * 1000) + 1

            calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
            monthYearText = findViewById(R.id.monthYearTV)
            dateTV = findViewById<TextView>(R.id.dateTV)
            previousBtn = findViewById(R.id.previousBtn)
            nextBtn = findViewById(R.id.nextBtn)
            homeTitle = findViewById(R.id.homeTitle)
            homeTitle.text = "현재 ${diff}일째 운동중"

            HomeCalendarUtils.selectedDate = LocalDate.now()
            setWeekView()

            previousBtn.setOnClickListener {
                HomeCalendarUtils.selectedDate = HomeCalendarUtils.selectedDate.minusWeeks(1)
                setWeekView()
            }

            nextBtn.setOnClickListener {
                HomeCalendarUtils.selectedDate = HomeCalendarUtils.selectedDate.plusWeeks(1)
                setWeekView()
            }

            CalendarData.getInstance().get { s, r ->
                Log.d("test", "들어옴")
                if (s == null || r == null) return@get
                records.clear()
                schedules.clear()
                records.addAll(r)
                schedules.addAll(s)

                var date = ArrayList<String>()
                for (schedule in schedules) {
                    date.add(schedule.date)
                }
                for (record in records) {
                    date.add(record.date.split("/")[0])
                }
                onUpdateTextView()
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setWeekView() {
        monthYearText.text = HomeCalendarUtils.monthYearFromDate(HomeCalendarUtils.selectedDate)
        val days: ArrayList<LocalDate> = HomeCalendarUtils.daysInWeekArray(HomeCalendarUtils.selectedDate)

        val calendarAdapter = HomeCalendarAdapter(days, this)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 7)
        calendarRecyclerView.layoutManager = layoutManager
        calendarRecyclerView.adapter = calendarAdapter
        //setEventAdapter()
    }

    @SuppressLint("NewApi")
    override fun onItemClick(position: Int, date: LocalDate?) {
        // 선택날짜 표시
        HomeCalendarUtils.selectedDate = date!!
        setWeekView()

        onUpdateTextView()
    }

    fun onUpdateTextView() {
        var date = HomeCalendarUtils.selectedDate.toString()
        var text = date + "\n\n"
        text += "-- 스케줄 --\n"

        for (schedule in schedules) {
            if (schedule.date == date) {
                text += "${schedule.exercise_name} ${schedule.exercise_set}세트\n"
            }
        }
        text += "\n-- 운동 --\n"

        var map = HashMap<String, Int>()

        for (record in records) {
            if (record.date.split("/")[0] == date) {
                if (map.containsKey(record.exercise_name)) {
                    map[record.exercise_name] = map[record.exercise_name]!! + 1
                }
                else map[record.exercise_name] = 1
                //text += "${record.exercise_name} ${record.exercise_count}개\n"
            }
        }

        for (m in map.keys) {
            text += "${m} ${map[m]}세트\n"
        }
        dateTV.text = text
    }
}