package com.jm.schoolproject.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jm.schoolproject.R
import com.jm.schoolproject.SerialBitmap
import com.jm.schoolproject.server.FeedbackDB
import com.jm.schoolproject.server.FeedbackDetail

class FeedbackPageItemActivity : AppCompatActivity() {
    private var feedback_id = 0
    private lateinit var feedback : FeedbackDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedbackpage_item)

        val feedbackIV = findViewById<ImageView>(R.id.feedbackIV)
        val feedbackTV = findViewById<TextView>(R.id.feedbackTV)

        feedback_id = intent.getIntExtra("feedback_id", 0)

        FeedbackDB.getDetail(feedback_id) {
            feedback = it!!
            val imageBytes = Base64.decode(feedback.feedback_image, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            feedbackIV.setImageBitmap(decodedImage)
            for (s in feedback.feedback_result.split("\\n")) {
                feedbackTV.text = feedbackTV.text.toString() + s + "\n"
            }
        }

    }
}