package com.jm.schoolproject.server

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class CalendarDB {
    companion object {
        fun get(user_id: String, callback: (Calendar?) -> Unit) {
            Log.d("test", "hi")
            val api = CalendarService.create()
            api.get(user_id).enqueue(object :
                Callback<Calendar> {
                override fun onResponse(call: Call<Calendar>, response: Response<Calendar>) {
                    if(response.isSuccessful) {
                        if (response.body()!!.MESSAGE == "Success!") {
                            callback(response.body())
                            return
                        }
                    }
                    callback(null)
                }
                override fun onFailure(call: Call<Calendar>, t: Throwable) {
                    Log.d("DB", "get Fail!")
                    callback(null)
                }
            })
        }
    }
}