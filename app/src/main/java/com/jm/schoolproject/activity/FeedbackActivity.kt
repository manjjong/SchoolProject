package com.jm.schoolproject.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageAnalysis
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import com.jm.schoolproject.FeedbackPose
import com.jm.schoolproject.R
import com.jm.schoolproject.camera.CameraAnalyzer
import com.jm.schoolproject.camera.CustomCamera
import com.jm.schoolproject.databinding.ActivityExerciseBinding
import com.jm.schoolproject.mlkit.pose.*

class FeedbackActivity : AppCompatActivity() {

    private lateinit var camera : CustomCamera
    private lateinit var binding : ActivityExerciseBinding
    private lateinit var analyzer : ImageAnalysis.Analyzer
    private lateinit var poseMatcher: PoseMatcher
    private lateinit var textCount : TextView
    private lateinit var textGrade : TextView
    private var id : Int = 0                            // 운동 아이디

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        textCount = findViewById(R.id.count)
        textGrade = findViewById(R.id.grade)
        id = intent.getIntExtra("id", 0)
        if (id == 1) poseMatcher = SquatPoseMatcher()
        else if (id == 2) poseMatcher = LeftLungePoseMatcher()
        else if (id == 3) poseMatcher = RightLungePoseMatcher()
        else if(id == 4) poseMatcher = LeftDumbbellCurlPoseMatcher()
        else if(id == 5) poseMatcher = RightDumbbellCurlPoseMatcher()
        else if(id == 6) poseMatcher = LeftPushUpMatcher()
        else if(id == 7) poseMatcher = RightPushUpMatcher()
        else if(id == 8) poseMatcher = BridgePoseMatcher()
        else if(id == 9) poseMatcher = LeftSideSquatPoseMatcher()
        else if(id == 10) poseMatcher = RightSideSquatPoseMatcher()
        else if(id == 11) poseMatcher = LeftBenchDipsPoseMatcher()
        else if(id == 12) poseMatcher = RightBenchDipsPoseMatcher()
        else if(id == 13) poseMatcher = MountainClimberPoseMatcher()
        else if(id == 14) poseMatcher = WideSquatPoseMatcher()
        Log.d("test", "id : $id")
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
            var result = poseMatcher.matchWithFeedback(pose, bitmap)

            when {
                result.second == 0 -> {
                    textCount.visibility = TextView.VISIBLE
                    textCount.text = "준비 자세를 취해주세요."
                }
                result.second == 2 -> {
                    textCount.text = "시작하세요!"
                    Handler().postDelayed({
                        textCount.visibility = View.INVISIBLE
                    }, 1000L)
                }
                result.second == 1 && result.first != null -> {
                    if (result.first!!.landmarks.isNotEmpty())
                        moveActivity(result.first!!)
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

    fun moveActivity(result : FeedbackPose) {
        val intent = Intent(applicationContext, FeedbackResultActivity::class.java)
        intent.putExtra("feedback", result)
        intent.putExtra("exercise_id", id)
        startActivity(intent)
        camera.onDestroy()
        finish()
        //onDestroy()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        val alert = AlertDialog.Builder(this)
        alert.setTitle("종료")
        alert.setMessage("운동을 멈추시겠습니까?")

        alert.setPositiveButton("네") { dialog, which ->
            finish()
        }
        alert.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        camera.onDestroy()
    }
}