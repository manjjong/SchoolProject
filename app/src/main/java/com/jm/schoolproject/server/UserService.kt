package com.jm.schoolproject.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*

interface UserService {
    @FormUrlEncoded
    @POST("/app/user/login/")
    fun loginUser(
        @Field("user_id") id: String,
        @Field("user_pw") password: String,
    ): Call<User>

    @FormUrlEncoded
    @POST("/app/user/register/")
    fun registerUser(
        @Field("user_id") id : String,
        @Field("user_pw") pw : String,
        @Field("user_name") name : String,
        @Field("user_age") age : Int,
        @Field("user_height") height : Int,
        @Field("user_kg") kg : Int,
        @Field("user_sex") sex : Int,
        @Field("register_day") day : String,
    ): Call<Message>

    companion object {
        private const val BASE_URL = "http://121.139.87.119:8000"

        fun create() : UserService {
            val gson : Gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
              //.client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(UserService::class.java)
        }
    }
}