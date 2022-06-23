package com.jm.schoolproject.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface GoalService {
    @FormUrlEncoded
    @POST("/app/challenge/")
    fun get(@Field("user_id") user_id: String): Call<GoalList>

    @FormUrlEncoded
    @POST("/app/challenge/start/")
    fun start(
        @Field("challenge_id") challenge_id: Int,
        @Field("user_id") user_id: String,
        @Field("exercise_id") exercise_id: Int,
        @Field("start_date") start_date: String,
        @Field("finish") finish: Boolean,
    ): Call<Message>

    companion object {
        private const val BASE_URL = "http://121.139.87.119:8000"

        fun create() : GoalService {
            val gson : Gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(GoalService::class.java)
        }
    }
}