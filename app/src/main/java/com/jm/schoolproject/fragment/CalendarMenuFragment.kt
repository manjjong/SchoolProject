package com.jm.schoolproject.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jm.schoolproject.CalendarData
import com.jm.schoolproject.ChallengeData
import com.jm.schoolproject.R
import com.jm.schoolproject.decorator.EventDecorator
import com.jm.schoolproject.decorator.OneDayDecorator
import com.jm.schoolproject.decorator.SaturdayDecorator
import com.jm.schoolproject.decorator.SundayDecorator
import com.jm.schoolproject.server.Record
import com.jm.schoolproject.server.Schedule
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class CalendarMenuFragment : Fragment() {
    @SuppressLint("InflateParams")
    private val oneDayDecorator: OneDayDecorator = OneDayDecorator()
    var cursor: Cursor? = null
    private lateinit var materialCalendarView: MaterialCalendarView
    private var schedules = ArrayList<Schedule>()
    private var records = ArrayList<Record>()
    private var sdf = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_calendar, null).apply {
            materialCalendarView = findViewById<View>(R.id.calendarView) as MaterialCalendarView
            materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()

            materialCalendarView.addDecorators(
                SundayDecorator(),
                SaturdayDecorator(),
                oneDayDecorator
            )

            Log.d("fragment", "onCreate")

            CalendarData.getInstance().get { s, r ->
                Log.d("test", "들어옴")
                if (s == null || r == null) return@get
                records.clear()
                schedules.clear()
                records.addAll(r)
                schedules.addAll(s)

                var date = ArrayList<String>()
                for (schedule in schedules) { date.add(schedule.date) }
                for (record in records) { date.add(record.date.split("/")[0]) }

                ApiSimulator(date, materialCalendarView, context).executeOnExecutor(Executors.newSingleThreadExecutor())
            }

            materialCalendarView.setOnDateChangedListener { widget, date, selected ->
                val year: Int = date.year
                val month: Int = date.month + 1
                val day: Int = date.day

                var day_of_week = date.calendar.get(Calendar.DAY_OF_WEEK)
                var day_of_week_txt = "$day."
                var day_of_week_color = "#000000"

                when (day_of_week) {
                    Calendar.MONDAY -> day_of_week_txt += "월"
                    Calendar.TUESDAY -> day_of_week_txt += "화"
                    Calendar.WEDNESDAY -> day_of_week_txt += "수"
                    Calendar.THURSDAY -> day_of_week_txt += "목"
                    Calendar.FRIDAY -> day_of_week_txt += "금"
                    Calendar.SATURDAY -> {
                        day_of_week_color = "#FF6200EE"
                        day_of_week_txt += "토"
                    }
                    Calendar.SUNDAY -> {
                        day_of_week_color = "#E91E1E"
                        day_of_week_txt += "일"
                    }
                }

                calendar_date.text = day_of_week_txt
                calendar_date.setTextColor(Color.parseColor(day_of_week_color))
                materialCalendarView.clearSelection()

                var empty = true
                var text = ""
                val calendar = Calendar.getInstance()
                calendar.set(year, month-1, day)
                var date = sdf.format(calendar.time)

                for (schedule in schedules) {
                    if (schedule.date == date) {
                        text += "${schedule.exercise_name} ${schedule.exercise_set}세트\n"
                    }
                }
                if (text.isBlank()) {
                    calendar_schedule_title.visibility = View.GONE
                    calendar_shedule_content.visibility = View.GONE
                }
                else {
                    calendar_schedule_title.visibility = View.VISIBLE
                    calendar_shedule_content.visibility = View.VISIBLE
                    calendar_shedule_content.text = text
                    empty = false
                }

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

                text = ""
                for (m in map.keys) {
                    text += "${m} ${map[m]}세트\n"
                }

                if (map.keys.size == 0) {
                    calendar_exercise_title.visibility = View.GONE
                    calendar_exercise_content.visibility = View.GONE
                }
                else {
                    calendar_exercise_title.visibility = View.VISIBLE
                    calendar_exercise_content.visibility = View.VISIBLE
                    calendar_exercise_content.text = text
                    empty = false
                }

                if (empty) calendar_no_content.visibility = View.VISIBLE
                else calendar_no_content.visibility = View.GONE
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("fragment", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("fragment", "onResume")
    }

    @SuppressLint("StaticFieldLeak")
    private class ApiSimulator(var Time_Result: ArrayList<String>, var calendar: MaterialCalendarView, var context : Context) :
        AsyncTask<Void?, Void?, List<CalendarDay>>() {
        override fun doInBackground(vararg p0: Void?): List<CalendarDay> {
            try {
                Thread.sleep(500)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            val calendar = Calendar.getInstance()
            val dates: ArrayList<CalendarDay> = ArrayList()

            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            for (i in Time_Result) {
                val day = CalendarDay.from(calendar)
                val time = i.split("-").toTypedArray()
                val year = time[0].toInt()
                val month = time[1].toInt()
                val dayy = time[2].toInt()
                dates.add(day)
                calendar[year, month - 1] = dayy
            }
            return dates
        }

        override fun onPostExecute(calendarDays: List<CalendarDay>) {
            super.onPostExecute(calendarDays)
            calendar.addDecorator(
                EventDecorator(Color.GRAY, calendarDays, context)
            )
        }
    }
}