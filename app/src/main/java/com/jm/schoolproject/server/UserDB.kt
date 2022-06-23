package com.jm.schoolproject.server

import android.graphics.Color
import android.util.Log
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import java.text.SimpleDateFormat
import java.time.LocalDateTime

class UserDB {
    companion object {
        fun login(id : String, password : String, callback : (User?) -> Unit) {
            val api = UserService.create()
            api.loginUser(id, password).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.isSuccessful) {
                        if (response.body()!!.MESSAGE == "Success!") {
                            callback(response.body())
                            return
                        }
                    }
                    callback(null)
                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("DB", "login Fail!")
                    callback(null)
                }
            })
        }

        fun test(id : String, password : String) : Boolean {
            var url = URL("http://121.139.87.119:8000/app/user/login/?user_id=$id&user_pw=$password")
            val connection = url.openConnection()
            val data = connection.getInputStream().readBytes().toString(charset("UTF-8"))
            var json = JSONObject(data)

            Log.d("test", json.toString())
            return false
        }

        fun register(id : String, password: String, name : String, age: Int, height : Int, kg : Int, sex : Int, callback : (Boolean?) -> Unit) {
            val api = UserService.create()
            var sdf = SimpleDateFormat("yyyy-MM-dd/hh-mm")
            val date = sdf.format(System.currentTimeMillis())

            api.registerUser(id, password, name, age, height, kg, sex, date).enqueue(object : Callback<Message> {
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    if(response.isSuccessful) {
                        var message = response.body()!!.MESSAGE
                        if (message == "Success!") {
                            Log.d("DB", "message : " + response.body()!!.MESSAGE)
                            callback(true)
                            return
                        }
                    }
                    Log.d("DB", "come in")
                    callback(false)
                }

                override fun onFailure(call: Call<Message>, t: Throwable) {
                    Log.d("DB", "register Fail!")
                    callback(false)
                }
            })
        }
    }
}