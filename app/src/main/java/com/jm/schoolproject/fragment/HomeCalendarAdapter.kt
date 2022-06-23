package com.jm.schoolproject.fragment

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.jm.schoolproject.R
import com.jm.schoolproject.fragment.HomeCalendarUtils.Companion.selectedDate
import java.time.LocalDate
import java.util.ArrayList

class HomeCalendarAdapter(
    private var days: ArrayList<LocalDate>,
    private var onItemListener: OnItemListener
) : RecyclerView.Adapter<HomeCalendarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCalendarViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.home_calendarcell, parent, false)
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        if (days.size > 15)
            layoutParams.height = (parent.height * 0.166666666).toInt()
        else layoutParams.height = parent.height
        return HomeCalendarViewHolder(view, onItemListener, days)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HomeCalendarViewHolder, position: Int) {
        val date: LocalDate = days[position]
        if(date == null)
            holder.dayOfMonth.text = ""
        else{
            holder.dayOfMonth.text = date.dayOfMonth.toString()
            if(date == selectedDate)
                holder.parentView.setBackgroundColor(Color.LTGRAY)
        }
    }

    override fun getItemCount(): Int {
        return days.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, date: LocalDate?)
    }
}