package com.jm.schoolproject.server

import android.media.Image

data class Message(
    val MESSAGE : String,
)

data class User (
    val MESSAGE : String,
    val user_id : String,
    val user_pw : String,
    val user_name : String,
    val user_age : Int,
    val user_height : Int,
    val user_kg : Int,
    val user_sex : Int,
    val register_day : String,
)

data class Challenge (
    val challenge_id : Int,
    val period : Int,
    val cycle : Int,
    val set : Int,
    val exercise_id : Int,
    val exercise_name : String,
    val description : String,
    val available : Boolean,
)

data class GoalList (
    val MESSAGE : String,
    val challenge : List<Challenge>
)

data class Calendar (
    val MESSAGE : String,
    val schedule: List<Schedule>,
    val record: List<Record>,
)

data class Schedule (
    val date : String,
    val exercise_id: Int,
    val exercise_name: String,
    val exercise_set: Int,
)

data class Record (
    val date : String,
    val exercise_id: Int,
    val exercise_name: String,
    val exercise_count: Int,
)

data class FeedbackSummaryMessage(
    val MESSAGE: String,
    val summary: List<FeedbackSummary>
)

data class FeedbackDetailMessage(
    val MESSAGE: String,
    val feedback: FeedbackDetail,
)

data class FeedbackSummary (
    val feedback_id: Int,
    val exercise_id: Int,
    val exercise_name: String,
    val feedback_date: String,
)

data class FeedbackDetail (
    val feedback_id: Int,
    val exercise_id: Int,
    val exercise_name: String,
    val feedback_date: String,
    val feedback_result: String,
    val feedback_image : String,
)