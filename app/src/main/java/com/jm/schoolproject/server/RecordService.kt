package com.jm.schoolproject.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RecordService {
    @FormUrlEncoded
    @POST("/app/record/add/")
    fun addRecord(
        @Field("record_date") record_date: String,
        @Field("exercise_id") exercise_id: Int,
        @Field("exercise_count") exercise_count: Int,
        @Field("user_id") user_id: String,
    ): Call<Message>

    companion object {
        private const val BASE_URL = "http://121.139.87.119:8000"

        fun create() : RecordService {
            val gson : Gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RecordService::class.java)
        }
    }
}