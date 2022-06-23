package com.jm.schoolproject.fragment

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jm.schoolproject.R
import java.time.LocalDate
import java.util.ArrayList

class HomeCalendarViewHolder(
    itemView: View, private val onItemListener: HomeCalendarAdapter.OnItemListener, days: ArrayList<LocalDate>
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val days: ArrayList<LocalDate>
    val parentView: View = itemView.findViewById(R.id.parentView)
    val dayOfMonth: TextView = itemView.findViewById(R.id.cellDayText)

    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, days[adapterPosition])
    }

    init {
        itemView.setOnClickListener(this)
        this.days = days
    }
}