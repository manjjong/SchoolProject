package com.jm.schoolproject.fragment

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.ArrayList

class HomeCalendarUtils {
    companion object{
        lateinit var selectedDate: LocalDate

        @RequiresApi(Build.VERSION_CODES.O)
        fun formattedDate(date: LocalDate): String {
            val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
            return date.format(formatter)
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        fun formattedTime(time: LocalTime): String {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a")
            return time.format(formatter)
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        fun monthYearFromDate(date: LocalDate): String {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
            return date.format(formatter)
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        fun parse(date: LocalDate): String {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MMMM-dd")
            return date.format(formatter)
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        fun daysInMonthArray(date: LocalDate?): ArrayList<LocalDate?> {
            val daysInMonthArray = ArrayList<LocalDate?>()
            val yearMonth = YearMonth.from(date)
            val daysInMonth = yearMonth.lengthOfMonth()
            val firstOfMonth = Companion.selectedDate.withDayOfMonth(1)
            val dayOfWeek = firstOfMonth.dayOfWeek.value
            for (i in 1..42) {
                if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) daysInMonthArray.add(null) else daysInMonthArray.add(
                    LocalDate.of(
                        Companion.selectedDate.year, Companion.selectedDate.month, i - dayOfWeek
                    )
                )
            }
            return daysInMonthArray
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        fun daysInWeekArray(selectedDate: LocalDate): ArrayList<LocalDate> {
            val days = ArrayList<LocalDate>()
            var current: LocalDate? = sundayForDate(selectedDate)
            val endDate: LocalDate = current!!.plusWeeks(1)

            while (current!!.isBefore(endDate)) {
                days.add(current)
                current = current.plusDays(1)
            }
            return days
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private fun sundayForDate(current: LocalDate): LocalDate? {
            var current = current
            val oneWeekAgo = current.minusWeeks(1)

            while (current.isAfter(oneWeekAgo)) {
                if (current.dayOfWeek == DayOfWeek.SUNDAY)
                    return current

                current = current.minusDays(1)
            }
            return null
        }
    }
}