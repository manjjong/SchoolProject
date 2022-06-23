package com.jm.schoolproject

import android.util.Log
import android.widget.Toast
import com.jm.schoolproject.server.User
import com.jm.schoolproject.server.UserDB

class UserData {
    var user_id : String = ""
    var user_pw : String = ""
    var user_name : String = ""
    var user_age = -1
    var user_height = -1
    var user_kg = -1
    var user_sex = -1
    var register_day = ""
    var state = LOGOUT_STATE

    fun login(callback : (Boolean?) -> Unit) {
        user_id = MainActivity.prefs.getString("user_id", "")
        user_pw = MainActivity.prefs.getString("user_pw", "")

        if (user_id.isEmpty() || user_pw.isEmpty()) {
            state = LOGOUT_STATE
            callback(true)
        } else {
            UserDB.login(user_id, user_pw, callback = {
                if (it != null) {
                    state = LOGIN_STATE
                    user_name = it.user_name
                    user_age = it.user_age
                    user_height = it.user_height
                    user_kg = it.user_kg
                    user_sex = it.user_sex
                    register_day = it.register_day

                    Log.d("DB", "LOGIN : $user_name, $user_age, $user_height, ")
                }
                else {
                    state = LOGOUT_STATE
                }
                callback(true)
            })
        }
    }

    fun logout() {
        if (state == LOGOUT_STATE) return
        state = LOGOUT_STATE
        MainActivity.prefs.setString("user_id", "")
        MainActivity.prefs.setString("user_pw", "")
        user_id = ""
        user_pw = ""
    }

    fun login(user_id : String, user_pw : String) {
        UserDB.login(user_id, user_pw, callback = {
            if (it != null) {
                state = LOGIN_STATE
                user_name = it.user_name
                user_age = it.user_age
                user_height = it.user_height
                user_kg = it.user_kg
                user_sex = it.user_sex
                register_day = it.register_day

                Log.d("DB", "LOGIN : $user_name, $user_age, $user_height, ")
            }
            else {
                state = LOGOUT_STATE
            }
        })
    }

    fun login(user : User) {
        user_id = user.user_id
        user_pw = user.user_pw
        user_name = user.user_name
        user_age = user.user_age
        user_height = user.user_height
        user_kg = user.user_kg
        user_sex = user.user_sex
        register_day = user.register_day
        state = LOGIN_STATE

        MainActivity.prefs.setString("user_id", user_id)
        MainActivity.prefs.setString("user_pw", user_pw)
    }

    companion object {
        @Volatile private var instance: UserData?= null
        val LOGIN_STATE = 1
        val LOGOUT_STATE = 0

        @JvmStatic fun getInstance() : UserData =
            instance ?: synchronized(this) {
                instance ?: UserData().also {
                    instance = it
                }
            }
    }
}