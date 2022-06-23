package com.jm.schoolproject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.jm.schoolproject.R

class  FeedbackMenuFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var feedbackAdapter: FeedbackAdapter
    private lateinit var exerciseDatas : ArrayList<ExerciseData>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exercise, null).apply {
            recyclerView = findViewById<RecyclerView>(R.id.Recycler1)
            feedbackAdapter = activity?.let {FeedbackAdapter(it)}!!
            exerciseDatas = ArrayList()

            exerciseDatas.add(ExerciseData(1, "스쿼트"))
            exerciseDatas.add(ExerciseData(2, "런지"))
            exerciseDatas.add(ExerciseData(4, "덤벨컬"))
            exerciseDatas.add(ExerciseData(6, "푸쉬업"))
            exerciseDatas.add(ExerciseData(8, "브릿지"))
            exerciseDatas.add(ExerciseData(9, "푸쉬업"))
            exerciseDatas.add(ExerciseData(10, "숄더프레스"))
            exerciseDatas.add(ExerciseData(11, "플랭크"))
            exerciseDatas.add(ExerciseData(12, "사이드레터럴레이즈"))
            feedbackAdapter.datas = exerciseDatas
            recyclerView.adapter = feedbackAdapter
        }
    }
}