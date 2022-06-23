package com.jm.schoolproject.server

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import java.text.SimpleDateFormat

class RecordDB {
    companion object {
        fun add(exercise_id: Int, exercise_count: Int, user_id: String, callback: (Boolean?) -> Unit) {
            var sdf = SimpleDateFormat("yyyy-MM-dd/hh-mm")
            val date = sdf.format(System.currentTimeMillis())
            val api = RecordService.create()
            api.addRecord(date, exercise_id, exercise_count, user_id).enqueue(object : Callback<Message> {
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    if(response.isSuccessful) {
                        if (response.body()!!.MESSAGE == "Success!") {
                            callback(true)
                            return
                        }
                    }
                    callback(false)
                }
                override fun onFailure(call: Call<Message>, t: Throwable) {
                    Log.d("DB", "get Fail!")
                    callback(false)
                }
            })
        }
    }
}