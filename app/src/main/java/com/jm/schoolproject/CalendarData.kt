package com.jm.schoolproject

import com.jm.schoolproject.server.*

class CalendarData {
    var schedules = ArrayList<Schedule>()
    var records = ArrayList<Record>()

    fun get(callback : (ArrayList<Schedule>?, ArrayList<Record>?) -> Unit) {
        schedules.clear()
        records.clear()

        CalendarDB.get(UserData.getInstance().user_id) {
            if (it == null) {
                callback(null, null)
                return@get
            }
            schedules.addAll(it!!.schedule)
            records.addAll(it!!.record)
            callback(schedules, records)
        }
    }

    companion object {
        @Volatile private var instance: CalendarData?= null

        @JvmStatic fun getInstance() : CalendarData =
            instance ?: synchronized(this) {
                instance ?: CalendarData().also {
                    instance = it
                }
            }
    }
}