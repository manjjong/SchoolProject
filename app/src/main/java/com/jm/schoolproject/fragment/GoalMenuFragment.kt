package com.jm.schoolproject.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.jm.schoolproject.ChallengeData
import com.jm.schoolproject.R
import com.jm.schoolproject.server.Challenge

class GoalMenuFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var goalAdapter: GoalAdapter
    private lateinit var goalDatas : ArrayList<Challenge>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_goal, null).apply {
            recyclerView = findViewById<RecyclerView>(R.id.Recycler1)
            goalAdapter = activity?.let {GoalAdapter(it)}!!
            goalDatas = ArrayList()

            ChallengeData.getInstance().get {
                if (it == null) return@get
                goalDatas.clear()
                goalDatas.addAll(it)
                goalAdapter.datas = goalDatas
                recyclerView.adapter = goalAdapter
            }
        }
    }
}