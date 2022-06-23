package com.jm.schoolproject.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.jm.schoolproject.R
import com.jm.schoolproject.UserData
import com.jm.schoolproject.server.FeedbackDB
import com.jm.schoolproject.server.FeedbackSummary

class FeedbackPageActivity: AppCompatActivity() {
    var datas: ArrayList<FeedbackSummary> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedbackpage)

        val recycler = findViewById<RecyclerView>(R.id.Recycler1)
        recycler.addItemDecoration(DividerItemDecoration(applicationContext, 1))
        val myPageFeedbackAdapter = FeedbackPageAdapter(this)


        FeedbackDB.getSummary(UserData.getInstance().user_id) {
            datas.clear()
            datas.addAll(it!!)
            myPageFeedbackAdapter.datas = datas
            recycler.adapter = myPageFeedbackAdapter
        }


    }
}