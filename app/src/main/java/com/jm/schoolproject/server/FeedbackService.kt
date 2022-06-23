package com.jm.schoolproject.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface FeedbackService {
    @Multipart
    @POST("/app/feedback/save/")
    fun saveFeedback(
        @Part("user_id") id: String,
        @Part("exercise_id") exercise_id: Int,
        @Part("feedback_result") feedback_result: String,
        @Part("feedback_date") feedback_date: String,
        @Part feedback_img: MultipartBody.Part,
    ): Call<Message>

    @FormUrlEncoded
    @POST("/app/feedback/summary/")
    fun summary(
        @Field("user_id") id: String,
    ): Call<FeedbackSummaryMessage>

    @FormUrlEncoded
    @POST("/app/feedback/detail/")
    fun detail(
        @Field("feedback_id") feedback_id: Int,
    ): Call<FeedbackDetailMessage>


    companion object {
        private const val BASE_URL = "http://121.139.87.119:8000"

        fun create() : FeedbackService {
            val gson : Gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(FeedbackService::class.java)
        }
    }
}