package com.jm.schoolproject

import com.jm.schoolproject.server.Challenge
import com.jm.schoolproject.server.GoalDB

class ChallengeData {
    var datas = ArrayList<Challenge>()
    var check = false

    fun get(callback : (ArrayList<Challenge>?) -> Unit) {
        datas.clear()
        GoalDB.get(UserData.getInstance().user_id) {
            if (it == null) {
                callback(null)
                return@get
            }
            datas.addAll(it!!.challenge)
            check = true
            callback(datas)
        }
    }

    companion object {
        @Volatile private var instance: ChallengeData?= null

        @JvmStatic fun getInstance() : ChallengeData =
            instance ?: synchronized(this) {
                instance ?: ChallengeData().also {
                    instance = it
                }
            }
    }
}