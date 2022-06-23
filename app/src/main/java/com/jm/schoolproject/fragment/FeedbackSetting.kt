package com.jm.schoolproject.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import com.jm.schoolproject.R
import com.jm.schoolproject.activity.ExerciseActivity
import com.jm.schoolproject.activity.FeedbackActivity

class FeedbackSetting : Activity() {
    private lateinit var switchLayout : LinearLayout
    private lateinit var switchLeft : TextView
    private lateinit var switchMedium : Switch
    private lateinit var switchRight : TextView

    private lateinit var btn : Button
    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_feedback_setting)

        id = intent.getIntExtra("id", 0)
        switchLayout = findViewById(R.id.fbs_llayout)
        switchLeft = findViewById(R.id.fbs_ltv)
        switchMedium = findViewById(R.id.fbs_msw)
        switchRight = findViewById(R.id.fbs_rtv)

        btn = findViewById(R.id.fbs_btn)
        
        if (id == 4) {
            switchLeft.text = "왼손"
            switchRight.text = "오른손"
        } else if (id == 6) {
            switchLeft.text = "왼쪽 측면"
            switchRight.text = "오른쪽 측면"
        }

        if (id != 2 && id != 4 && id != 6) {
            val intent = Intent(this, FeedbackActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
            finish()
        }

        btn.setOnClickListener {
            val intent = Intent(this, FeedbackActivity::class.java)

            if (id == 2 || id == 4 || id == 6) {
                var switch = 0
                if(switchMedium.isChecked) switch = 1
                id += switch
            }
            intent.putExtra("id", id)
            startActivity(intent)
            finish()
        }
    }
}