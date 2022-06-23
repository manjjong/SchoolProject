package com.jm.schoolproject.activity

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jm.schoolproject.FeedbackPose
import com.jm.schoolproject.R
import com.jm.schoolproject.SerialBitmap
import com.jm.schoolproject.UserData
import com.jm.schoolproject.server.FeedbackDB
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink

/*
var url = URL()
val connection = url.openConnection()
val data = connection.getInputStream().readBytes().toString(charset("UTF-8"))
return JSONObject(data)
 */

class FeedbackResultActivity : AppCompatActivity() {
    private var line = arrayOf(Line(20, 16), Line(16, 14), Line(14, 12),
    Line(12, 11), Line(11, 23), Line(23, 24), Line(24, 12),
    Line(24, 26), Line(26, 28), Line(28, 32), Line(32, 30), Line(28, 30),
    Line(23, 25), Line(25, 27), Line(27, 29), Line(29, 31), Line(27, 31),
    Line(11, 13), Line(13, 15), Line(15, 21), Line(15, 19))
    private lateinit var feedbackPose : FeedbackPose
    private lateinit var originalPose : FeedbackPose
    private lateinit var imageView: ImageView
    private lateinit var textView : TextView
    private var spots = ArrayList<Int>()
    private var lines = ArrayList<Line>()
    private var exercise_id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_result)
        feedbackPose = intent.getSerializableExtra("feedback") as FeedbackPose
        exercise_id = intent.getIntExtra("exercise_id", 0)
        //originalPose = intent.getParcelableExtra<FeedbackPose>("original")!!
        imageView = findViewById(R.id.feedbackIV)
        textView = findViewById(R.id.feedbackTV)

        var tmp = ArrayList<String>()
        feedbackPose.feedback.forEach {
            if(!tmp.contains(it.getMessage())) {
                textView.text = textView.text.toString() + it.getMessage() + "\n"
                tmp.add(it.getMessage())
            }
            if (it.lastLandmarkType != -1 && it.middleLandmarkType != -1 && it.firstLandmarkType != -1) {
                spots.add(it.lastLandmarkType)
                spots.add(it.middleLandmarkType)
                spots.add(it.firstLandmarkType)

                lines.add(Line(it.lastLandmarkType, it.middleLandmarkType))
                lines.add(Line(it.middleLandmarkType, it.firstLandmarkType))
            }
            else if (it.lastLandmarkType != -1) spots.add(it.lastLandmarkType)
            else if (it.middleLandmarkType != -1) spots.add(it.middleLandmarkType)
            else if (it.firstLandmarkType != -1) spots.add(it.firstLandmarkType)
        }
        onDraw()
    }

    private fun onDraw() {
        //var bitmap : Bitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)
        var bitmap = SerialBitmap.translate(feedbackPose.bitmapData)
        var canvas : Canvas = Canvas(bitmap)
        var paint : Paint = Paint()

        var tmp = spots.distinct()

        paint.color = Color.BLACK
        paint.strokeWidth = 5f
        line.forEach { target->
            paint.color = Color.BLACK
            for (l in lines) {
                Log.d("test", "l.start : " + l.start + ", l.end : " + l.end)
                if ((l.start == target.start && l.end == target.end) ||
                    (l.start == target.end && l.end == target.start)) {
                        Log.d("test", "들어옴")
                    paint.color = Color.RED
                    break
                }
            }
            canvas.drawLine(feedbackPose.landmarks[target.start].coordinate.x,
                feedbackPose.landmarks[target.start].coordinate.y,
                feedbackPose.landmarks[target.end].coordinate.x,
                feedbackPose.landmarks[target.end].coordinate.y, paint)
        }

        feedbackPose.landmarks.forEach { target ->
            if (target.i >= 11) {
                paint.color = Color.BLUE
                if (tmp.contains(target.i)) paint.color = Color.RED
                paint.strokeWidth = 10f
                canvas.drawPoint(target.coordinate.x, target.coordinate.y, paint)
            }
        }

        imageView.setImageBitmap(bitmap)
        // blob형태 bitmap 변형


        FeedbackDB.saveFeedback(UserData.getInstance().user_id, exercise_id, textView.text.toString(), SerialBitmap.translate(bitmap), callback = {
            if (it) {
                Log.d("test", "성공")
            }
        })
        /*
        FeedbackDB.testFeedback(UserData.getInstance().user_id, exercise_id, textView.text.toString(), callback = {
            Log.d("test", "test")
        })
        */
    }

    inner class Line(var start : Int, var end : Int)


}