package com.jm.schoolproject.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jm.schoolproject.MainActivity
import com.jm.schoolproject.R
import com.jm.schoolproject.UserData
import com.jm.schoolproject.server.UserDB
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginBtn.setOnClickListener {
            val userID = findViewById<EditText>(R.id.userID).text.toString()
            val userPasswd = findViewById<EditText>(R.id.userPasswd).text.toString()
            loginBtn.isEnabled = false

            CoroutineScope(Dispatchers.IO).launch {
                doLogin(userID, userPasswd)
            }
        }

        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun doLogin(userID: String, userPasswd:String){
        //계정 확인
        //로그인 성공 시 MainActivity로
        //val intent = Intent(this, MainActivity::class.java)
        //startActivity(intent)
        //finish()
        //UserData.getInstance().login(userID, userPasswd)

        UserDB.login(userID, userPasswd, callback = {
            if (it != null) {
                Toast.makeText(this, "성공!", Toast.LENGTH_SHORT).show()
                UserData.getInstance().login(it)
                var intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else {
                Toast.makeText(this, "실패!", Toast.LENGTH_SHORT).show()
                loginBtn.isEnabled = true
            }
        })

    }
}