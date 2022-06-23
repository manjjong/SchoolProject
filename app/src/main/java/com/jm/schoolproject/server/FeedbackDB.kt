package com.jm.schoolproject.server

import android.graphics.Bitmap
import android.util.Log
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat

class FeedbackDB {
    companion object {
        fun getSummary(id : String, callback : (List<FeedbackSummary>?) -> Unit) {
            val api = FeedbackService.create()

            api.summary(id).enqueue(object : Callback<FeedbackSummaryMessage> {
                    override fun onResponse(call: Call<FeedbackSummaryMessage>, response: Response<FeedbackSummaryMessage>) {
                        if (response.body()!!.MESSAGE == "Success!") {
                            callback(response.body()!!.summary)
                            return
                        }
                       callback(null)
                    }

                    override fun onFailure(call: Call<FeedbackSummaryMessage>, t: Throwable) {
                        callback(null)
                    }
                })
        }

        fun getDetail(feedback_id : Int, callback : (FeedbackDetail?) -> Unit) {
            val api = FeedbackService.create()

            api.detail(feedback_id).enqueue(object : Callback<FeedbackDetailMessage> {
                override fun onResponse(call: Call<FeedbackDetailMessage>, response: Response<FeedbackDetailMessage>) {
                    if (response.body()!!.MESSAGE == "Success!") {
                        callback(response.body()!!.feedback)
                        return
                    }
                    callback(null)
                }

                override fun onFailure(call: Call<FeedbackDetailMessage>, t: Throwable) {
                    callback(null)
                }
            })
        }

        fun saveFeedback(id : String, exercise : Int, result: String, byteArray : ByteArray, callback : (Boolean) -> Unit) {
            val api = FeedbackService.create()
            var sdf = SimpleDateFormat("yyyy-MM-dd/hh-mm")
            val date = sdf.format(System.currentTimeMillis())
            val fileBody = RequestBody.create(MediaType.parse("image/*"), byteArray)
            val multipartBody : MultipartBody.Part? =
                MultipartBody.Part.createFormData("feedback_img", "feedback_img.jpg", fileBody)

            api.saveFeedback(id, exercise, result, date, multipartBody!!)
                .enqueue(object : Callback<Message> {
                    override fun onResponse(
                        call: Call<Message>,
                        response: Response<Message>
                    ) {
                        Log.d("test", "hi")
                        callback(true)
                    }

                    override fun onFailure(call: Call<Message>, t: Throwable) {
                        Log.d("test", "hi2")
                        callback(false)
                    }
                })
        }
    }
}