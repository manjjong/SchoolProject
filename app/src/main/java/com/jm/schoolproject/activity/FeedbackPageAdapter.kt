package com.jm.schoolproject.activity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jm.schoolproject.R
import com.jm.schoolproject.server.FeedbackSummary

class FeedbackPageAdapter(private val context: Context) : RecyclerView.Adapter<FeedbackPageAdapter.ViewHolder>() {
    var datas = listOf<FeedbackSummary>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackPageAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.feedbackpage_listview,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val feedback: TextView = itemView.findViewById(R.id.mypage_feedback)
        private val date: TextView = itemView.findViewById(R.id.mypage_date)
        fun bind(item: FeedbackSummary) {
            feedback.text = "${item.exercise_name} 피드백"
            date.text = "${item.feedback_date}"
            itemView.setOnClickListener {
                val intent = Intent(context, FeedbackPageItemActivity::class.java)
                intent.putExtra("feedback_id", item.feedback_id)
                context.startActivity(intent)
            }
        }
    }
}