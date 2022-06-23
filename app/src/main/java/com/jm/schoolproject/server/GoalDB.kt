package com.jm.schoolproject.server

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class GoalDB {
    companion object {
        fun get(user_id : String, callback: (GoalList?) -> Unit) {
            val api = GoalService.create()
            api.get(user_id).enqueue(object : Callback<GoalList> {
                override fun onResponse(call: Call<GoalList>, response: Response<GoalList>) {
                    if(response.isSuccessful) {
                        if (response.body()!!.MESSAGE == "Success!") {
                            callback(response.body())
                            return
                        }
                    }
                    callback(null)
                }
                override fun onFailure(call: Call<GoalList>, t: Throwable) {
                    Log.d("DB", "get Fail!")
                    callback(null)
                }
            })
        }

        fun start(challenge_id : Int, user_id : String, exercise_id : Int, callback: (Boolean) -> Unit) {
            val api = GoalService.create()
            var sdf = SimpleDateFormat("yyyy-MM-dd/hh-mm")
            val date = sdf.format(System.currentTimeMillis())
            api.start(challenge_id, user_id, exercise_id, date, false).enqueue(object : Callback<Message> {
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