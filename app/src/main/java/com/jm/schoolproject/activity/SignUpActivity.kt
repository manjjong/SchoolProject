package com.jm.schoolproject.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jm.schoolproject.R
import com.jm.schoolproject.server.UserDB

class SignUpActivity : AppCompatActivity() {

    private var userGender : Int = 0
    private lateinit var userID : EditText
    private lateinit var userPasswd : EditText
    private lateinit var userName : EditText
    private lateinit var userAge : EditText
    private lateinit var userKg : EditText
    private lateinit var userHeight : EditText

    private lateinit var manRadioBtn : RadioButton
    private lateinit var womanRadioBtn : RadioButton

    private var result = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signupBtn = findViewById<Button>(R.id.signUpBtn)
        userID = findViewById<EditText>(R.id.userID)
        userPasswd = findViewById<EditText>(R.id.userPasswd)
        userName = findViewById<EditText>(R.id.userName)
        userAge = findViewById<EditText>(R.id.userAge)
        userKg = findViewById<EditText>(R.id.userKg)
        userHeight = findViewById<EditText>(R.id.userHeight)

        manRadioBtn = findViewById<RadioButton>(R.id.radioButton)
        womanRadioBtn = findViewById<RadioButton>(R.id.radioButton2)
        signupBtn.setOnClickListener {
            if (isEmptyEditTexts() || (!manRadioBtn.isChecked && !womanRadioBtn.isChecked)) {
                Toast.makeText(applicationContext, "빈칸을 채워주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            when {
                manRadioBtn.isChecked -> {
                    userGender = 0
                }
                womanRadioBtn.isChecked -> {
                    userGender = 1
                }
                else -> Toast.makeText(this, "성별을 체크하시오", Toast.LENGTH_SHORT).show()
            }

            signupBtn.isEnabled = false
            //회원가입 성공 시 DB에 저장하고 finish()
            UserDB.register(userID.text.toString(), userPasswd.text.toString(), userName.text.toString(), userAge.text.toString().toInt(), userHeight.text.toString().toInt(), userKg.text.toString().toInt(), userGender, callback = {
                if (it != null) {
                    if (it) {
                        Toast.makeText(applicationContext, "회원가입 완료!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else {
                        Toast.makeText(applicationContext, "이미 존재하는 ID입니다.", Toast.LENGTH_SHORT).show()
                        signupBtn.isEnabled = true
                    }
                }
            })
        }
    }

    fun isEmptyEditTexts() : Boolean {
        return (userID.text.isEmpty() || userPasswd.text.isEmpty() || userName.text.isEmpty() || userAge.text.isEmpty() || userKg.text.isEmpty() || userHeight.text.isEmpty())
    }
}