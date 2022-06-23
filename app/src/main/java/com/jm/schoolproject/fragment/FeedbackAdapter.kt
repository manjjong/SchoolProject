package com.jm.schoolproject.fragment

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jm.schoolproject.R
import com.jm.schoolproject.activity.FeedbackActivity

class FeedbackAdapter (
    private val context: Context
) : RecyclerView.Adapter<FeedbackAdapter.ViewHolder>() {
    var datas = listOf<ExerciseData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_exercise_listview,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val workout: TextView = itemView.findViewById(R.id.workout)
        fun bind(item: ExerciseData) {
            workout.text = item.exercise

            itemView.setOnClickListener {
                val intent = Intent(context, FeedbackSetting::class.java)
                intent.putExtra("id", item.id)
                context.startActivity(intent)
            }
        }
    }
}