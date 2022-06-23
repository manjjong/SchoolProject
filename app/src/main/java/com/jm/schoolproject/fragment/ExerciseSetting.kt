package com.jm.schoolproject.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import com.jm.schoolproject.R
import com.jm.schoolproject.activity.ExerciseActivity

class ExerciseSetting : Activity() {
    private lateinit var switchLayout : LinearLayout
    private lateinit var switchLeft : TextView
    private lateinit var switchMedium : Switch
    private lateinit var switchRight : TextView

    private lateinit var btnMinus : Button
    private lateinit var btnPlus : Button
    private lateinit var tvCount : TextView
    private lateinit var btnStart : Button
    private var count : Int = 15
    private var id : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_exercise_setting)

        id = intent.getIntExtra("id", 0)
        btnMinus = findViewById<Button>(R.id.btnMinus)
        btnPlus = findViewById<Button>(R.id.btnPlus)
        tvCount = findViewById<TextView>(R.id.tvCount)
        btnStart = findViewById<Button>(R.id.btnStart)
        switchLayout = findViewById(R.id.switchLayout)
        switchLeft = findViewById(R.id.switchLeft)
        switchMedium = findViewById(R.id.switchMedium)
        switchRight = findViewById(R.id.switchRight)

        if (id == 2 || id == 4 || id == 6) {
            switchLayout.visibility = View.VISIBLE
        }

        if (id == 4) {
            switchLeft.text = "왼손"
            switchRight.text = "오른손"
        } else if (id == 6) {
            switchLeft.text = "왼쪽 측면"
            switchRight.text = "오른쪽 측면"
        }

        btnMinus.setOnClickListener {
            count--
            tvCount.text = count.toString()
        }

        btnPlus.setOnClickListener {
            count++
            tvCount.text = count.toString()
        }

        btnStart.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            intent.putExtra("goal", count)
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