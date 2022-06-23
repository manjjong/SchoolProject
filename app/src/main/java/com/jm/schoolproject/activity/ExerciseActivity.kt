package com.jm.schoolproject.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageAnalysis
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import com.jm.schoolproject.R
import com.jm.schoolproject.UserData
import com.jm.schoolproject.camera.CameraAnalyzer
import com.jm.schoolproject.camera.CustomCamera
import com.jm.schoolproject.databinding.ActivityExerciseBinding
import com.jm.schoolproject.mlkit.pose.*
import com.jm.schoolproject.server.RecordDB

class ExerciseActivity : AppCompatActivity() {
    private lateinit var camera : CustomCamera
    private lateinit var binding : ActivityExerciseBinding
    private lateinit var analyzer : ImageAnalysis.Analyzer
    private lateinit var poseMatcher: PoseMatcher
    private lateinit var textCount : TextView
    private lateinit var textGrade : TextView
    private var count : Int = 0
    private var id : Int = 0                            // 운동 아이디
    private var goal : Int = 0                          // 목표 개수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        textCount = findViewById(R.id.count)
        textGrade = findViewById(R.id.grade)
        id = intent.getIntExtra("id", 0)
        goal = intent.getIntExtra("goal", 0)
        if (id == 1) poseMatcher = SquatPoseMatcher()
        else if(id == 2) poseMatcher = LeftLungePoseMatcher()
        else if(id == 3) poseMatcher = RightLungePoseMatcher()
        else if(id == 4) poseMatcher = LeftDumbbellCurlPoseMatcher()
        else if(id == 5) poseMatcher = RightDumbbellCurlPoseMatcher()
        else if(id == 6) poseMatcher = LeftPushUpMatcher()
        else if(id == 7) poseMatcher = RightPushUpMatcher()
        initEvent()
        startCamera()
        textCount.visibility = TextView.VISIBLE
        textCount.text = "준비 자세를 취해주세요."
    }

    private fun initAnalyzer() {
        val options by lazy {
            PoseDetectorOptions.Builder().setDetectorMode(PoseDetectorOptions.STREAM_MODE).build()
        }
        val poseDetector by lazy {
            PoseDetection.getClient(options)
        }
        val onPoseDetected: (pose: Pose, bitmap: Bitmap) -> Unit = { pose, bitmap ->
            var score = poseMatcher.matchWithScore(pose)

            Log.d("test", "" + score.second)
            when {
                score.second == 0 -> {
                    textCount.visibility = TextView.VISIBLE
                    textCount.text = "준비 자세를 취해주세요."
                }
                score.second == 2 -> {
                    textCount.text = "시작하세요!"
                    Handler().postDelayed({
                        textCount.visibility = View.INVISIBLE
                    }, 1000L)
                }
                score.second == 1 && score.first >= 0.0 -> {
                    Log.d("Squat", "score : " + score.first)
                    textGrade.visibility = TextView.VISIBLE
                    textCount.visibility = TextView.VISIBLE
                    textGrade.text = poseMatcher.getGrade(score.first)
                    if (!textGrade.text.equals("Bad!"))
                        count += 1
                    textCount.text = count.toString() + "개"
                    if (goal == count) {
                        Handler().postDelayed({
                            moveActivity()
                        }, 200L)
                    }
                    Handler().postDelayed({
                        textGrade.visibility = TextView.INVISIBLE
                        textCount.visibility = TextView.INVISIBLE
                    }, 1000L)
                }
            }
        }
        analyzer = CameraAnalyzer(poseDetector, onPoseDetected)
    }

    private fun startCamera() {
        initAnalyzer()
        camera = CustomCamera(this, analyzer)
        camera.startCamera(binding.preview.surfaceProvider)
    }

    private fun initEvent() {

    }

    fun moveActivity() {
        RecordDB.add(id, count, UserData.getInstance().user_id) {
            if (it!!) Toast.makeText(applicationContext, "운동 저장 완료!", Toast.LENGTH_SHORT).show()
            else Toast.makeText(applicationContext, "운동 저장 오류!", Toast.LENGTH_SHORT).show()
        }
        camera.onDestroy()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        camera.onDestroy()
    }
}