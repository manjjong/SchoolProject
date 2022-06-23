package com.jm.schoolproject.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.jm.schoolproject.R
import com.jm.schoolproject.activity.LoginActivity

class ExerciseMenuFragment : Fragment() {
    private lateinit var loginBtn : Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var exerciseDatas : ArrayList<ExerciseData>
//hi JM
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exercise, null).apply {
            recyclerView = findViewById<RecyclerView>(R.id.Recycler1)
            exerciseAdapter = activity?.let {ExerciseAdapter(it)}!!
            exerciseDatas = ArrayList()

            exerciseDatas.add(ExerciseData(1, "스쿼트"))
            exerciseDatas.add(ExerciseData(2, "런지"))
            exerciseDatas.add(ExerciseData(4, "덤벨컬"))
            exerciseDatas.add(ExerciseData(6, "푸쉬업"))
            exerciseDatas.add(ExerciseData(7, "브릿지"))
            exerciseDatas.add(ExerciseData(8, "푸쉬업"))
            exerciseDatas.add(ExerciseData(9, "숄더프레스"))
            exerciseDatas.add(ExerciseData(10, "플랭크"))
            exerciseDatas.add(ExerciseData(11, "사이드레터럴레이즈"))
            exerciseAdapter.datas = exerciseDatas
            recyclerView.adapter = exerciseAdapter
        }
    }
}