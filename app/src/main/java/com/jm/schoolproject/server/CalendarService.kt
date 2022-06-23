package com.jm.schoolproject.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CalendarService {
    @FormUrlEncoded
    @POST("/app/calendar/get/")
    fun get(
        @Field("user_id") user_id: String,
    ): Call<Calendar>

    companion object {
        private const val BASE_URL = "http://121.139.87.119:8000"

        fun create() : CalendarService {
            val gson : Gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(CalendarService::class.java)
        }
    }
}