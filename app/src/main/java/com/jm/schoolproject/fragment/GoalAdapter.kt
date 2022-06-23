package com.jm.schoolproject.fragment

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jm.schoolproject.R
import com.jm.schoolproject.server.Challenge

class GoalAdapter(private val context: Context) : RecyclerView.Adapter<GoalAdapter.ViewHolder>() {
    var datas = listOf<Challenge>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_goal_listview,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val exercise: TextView = itemView.findViewById(R.id.goal_exercise)
        private val schedule: TextView = itemView.findViewById(R.id.goal_schedule)
        private val description: TextView = itemView.findViewById(R.id.goal_description)

        fun bind(item: Challenge) {
            exercise.text = item.exercise_name
            schedule.text = "${item.period}일동안 ${item.cycle}일마다 ${item.set} 세트씩"
            description.text = item.description

            itemView.setOnClickListener {
                if (item.available) {
                    val intent = Intent(context, GoalSetting::class.java)
                    intent.putExtra("challenge_id", item.challenge_id)
                    intent.putExtra("exercise_id", item.exercise_id)
                    context.startActivity(intent)
                }
                else Toast.makeText(context, "이미 진행중인 도전입니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}