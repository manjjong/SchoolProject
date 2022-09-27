package com.jm.schoolproject.fragment

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.jm.schoolproject.CalendarData
import com.jm.schoolproject.R
import com.jm.schoolproject.UserData
import com.jm.schoolproject.server.GoalDB

class GoalSetting : Activity() {
    private var exercise_id = 0
    private var challenge_id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_goal_setting)

        val btnCancel = findViewById<Button>(R.id.cancelBtn)
        val btnOK = findViewById<Button>(R.id.okBtn)

        exercise_id = intent.getIntExtra("exercise_id", 0)
        challenge_id = intent.getIntExtra("challenge_id", 0)

        btnCancel.setOnClickListener {
            finish()
        }

        btnOK.setOnClickListener {
            //캘린더에 추가
            GoalDB.start(challenge_id, UserData.getInstance().user_id, exercise_id) {
                if (it) Toast.makeText(applicationContext, "등록 완료!", Toast.LENGTH_SHORT).show()
                else Toast.makeText(applicationContext, "에러 발생!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}