package com.jm.schoolproject.mlkit.pose

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import com.jm.schoolproject.Feedback
import com.jm.schoolproject.FeedbackPose
import com.jm.schoolproject.SerialBitmap
import com.jm.schoolproject.mlkit.TargetPose
import com.jm.schoolproject.mlkit.TargetShape

class MountainClimberPoseMatcher : PoseMatcher() {
    private lateinit var leftTargetPose : TargetPose
    private lateinit var rightTargetPose: TargetPose

    override fun initTargetPose() {
        leftTargetPose = TargetPose(listOf(
            TargetShape(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE, 80.0, "b", "s"),
            TargetShape(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_ANKLE, 60.0, "b", "s"),
            TargetShape(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_ELBOW, PoseLandmark.LEFT_WRIST, 180.0, "b", "s"),
            TargetShape(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_ANKLE, 180.0, "b", "s"),
            TargetShape(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_WRIST, 180.0, "b", "s"),
        ))

        rightTargetPose = TargetPose(listOf(
            TargetShape(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_ANKLE, 180.0, "b", "s"),
            TargetShape(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_ELBOW, PoseLandmark.LEFT_WRIST, 180.0, "b", "s"),
            TargetShape(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE, 80.0, "b", "s"),
            TargetShape(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_ANKLE, 60.0, "b", "s"),
            TargetShape(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_WRIST, 180.0, "b", "s"),
        ))
    }
    override fun initCountPose() {
        countPose = TargetPose(listOf(
            TargetShape(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_ELBOW, 60.0, "b", "s"),
            TargetShape(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_ELBOW, PoseLandmark.LEFT_WRIST, 180.0, "b", "s"),
            TargetShape(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE, 180.0, "b", "s"),
            TargetShape(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW, 60.0, "b", "s"),
            TargetShape(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_WRIST, 180.0, "b", "s"),
            TargetShape(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE, 180.0, "b", "s"),
        ))
    }

    private val allPoseLandmark = ArrayList<PoseLandmark>()
    private lateinit var bitmap : Bitmap
    var state = LEFT_STATE
    var foot = PoseLandmark.LEFT_FOOT_INDEX
    var score = 0.0
    var feedbacks = mutableListOf<TargetShape>()

    init {
        super.offset = 15.0
        start = false
    }

    override fun matchWithScore(pose: Pose): Pair<Double, Int> {
        if (!start) {
            if (match(pose, countPose)) {
                Log.d("Test", "이제 시작")
                start = true
                return Pair(-1.0, 2)
            }
            return Pair(-1.0, 0)
        }

        if (allPoseLandmark.isEmpty()) {
            allPoseLandmark.addAll(pose.allPoseLandmarks)
        }
        else {
            if(pose.allPoseLandmarks.isNotEmpty()) {
                //Log.d("Squat", "allPoseLandmark : " + allPoseLandmark[24].position3D.y + ", pose : " + pose.allPoseLandmarks[24].position3D.y)
                if (allPoseLandmark[foot].position.y > pose.allPoseLandmarks[foot].position.y) {
                    allPoseLandmark.clear()
                    allPoseLandmark.addAll(pose.allPoseLandmarks)
                }
            }
        }

        if(match(pose, countPose)) {
            if(allPoseLandmark.isNotEmpty()) {
                if (state == LEFT_STATE) {
                    score = countAndSquatMatch(allPoseLandmark, leftTargetPose)
                    state = RIGHT_STATE
                    foot = PoseLandmark.RIGHT_FOOT_INDEX
                }
                else {
                    score += countAndSquatMatch(allPoseLandmark, rightTargetPose)
                    state = LEFT_STATE
                    foot = PoseLandmark.LEFT_FOOT_INDEX
                    allPoseLandmark.clear()
                    return Pair(score / 2.0, 1)
                }
                allPoseLandmark.clear()
            }
        }
        return Pair(-1.0, -1)
    }

    override fun matchWithFeedback(pose : Pose, bitmap : Bitmap) : Pair<FeedbackPose?, Int> {
        if (!start) {
            if (match(pose, countPose)) {
                Log.d("Test", "이제 시작")
                start = true
                return Pair(null, 2)
            }
            return Pair(null, 0)
        }

        if (allPoseLandmark.isEmpty()) {
            allPoseLandmark.addAll(pose.allPoseLandmarks)
            this.bitmap = bitmap
        }
        else {
            if (pose.allPoseLandmarks.isNotEmpty()) {
                if (allPoseLandmark[foot].position.y > pose.allPoseLandmarks[foot].position.y) {
                    allPoseLandmark.clear()
                    allPoseLandmark.addAll(pose.allPoseLandmarks)
                    this.bitmap = bitmap
                }
            }
        }

        if (match(pose, countPose)) {
            if(allPoseLandmark.isNotEmpty()) {
                if (state == LEFT_STATE) {
                    var feedback: List<TargetShape>? =
                        feedbackSquat(allPoseLandmark, leftTargetPose) ?: return Pair(null, -1)

                    feedbacks.addAll(feedback!!)
                    state = RIGHT_STATE
                    foot = PoseLandmark.RIGHT_FOOT_INDEX
                }
                else {
                    var feedback: List<TargetShape>? =
                        feedbackSquat(allPoseLandmark, leftTargetPose) ?: return Pair(null, -1)
                    feedbacks.addAll(feedback!!)
                    state = LEFT_STATE
                    foot = PoseLandmark.LEFT_FOOT_INDEX
                    allPoseLandmark.clear()
                    return Pair(FeedbackPose(Feedback.translate(allPoseLandmark), feedbacks, SerialBitmap.translate(this.bitmap)), 1)
                }
                allPoseLandmark.clear()
            }
        }
        return Pair(null, -1)
    }

    private fun feedbackSquat(allPoseLandmark: ArrayList<PoseLandmark>, targetPose: TargetPose) : List<TargetShape>? {
        var feedback = ArrayList<TargetShape>()
        targetPose.targets.forEach { target ->
            val (firstLandmark, middleLandmark, lastLandmark) = Triple(allPoseLandmark[target.firstLandmarkType],
                allPoseLandmark[target.middleLandmarkType],
                allPoseLandmark[target.lastLandmarkType]
            )

            if (landmarkNotFound(firstLandmark, middleLandmark, lastLandmark)) {
                Log.d("CameraX", "LandmarkNotFound : " + target.angle)
                return null
            }

            val angle = calculateAngle(firstLandmark, middleLandmark, lastLandmark)
            val targetAngle = target.angle

            //Log.d("Squat", "angle : " + angle + ", target : " + targetAngle)
            Log.d("CameraX", "OffSet Over : " + target.angle + "->" + angle + ", " + kotlin.math.abs(angle - targetAngle))

            val gap = angle - targetAngle

            if (gap > offset + 20) return null
            if (gap < -offset - 20) return null
            if (gap > offset || gap < -offset) {
                var tmp = target.clone()
                tmp.setTargetAngle(angle)
                feedback.add(tmp)
            }
        }
        return feedback.distinct()
    }

    private fun countAndSquatMatch(allPoseLandmark: ArrayList<PoseLandmark>, targetPose: TargetPose): Double {
        var score : Double = 0.0
        targetPose.targets.forEach { target ->
            val (firstLandmark, middleLandmark, lastLandmark) = Triple(allPoseLandmark[target.firstLandmarkType],
                allPoseLandmark[target.middleLandmarkType],
                allPoseLandmark[target.lastLandmarkType]
            )

            if (landmarkNotFound(firstLandmark, middleLandmark, lastLandmark)) {
                Log.d("CameraX", "LandmarkNotFound : " + target.angle)
                return -1.0
            }

            val angle = calculateAngle(firstLandmark!!, middleLandmark!!, lastLandmark!!)
            val targetAngle = target.angle

            //Log.d("Squat", "angle : " + angle + ", target : " + targetAngle)
            Log.d("CameraX", "OffSet Over : " + target.angle + "->" + angle + ", " + kotlin.math.abs(angle - targetAngle))

            if (kotlin.math.abs(angle - targetAngle) > offset + 10) {
                return -1.0
            }
            score += kotlin.math.abs(angle - targetAngle)
            //Log.d("Squat", "offset : " + abs(angle - targetAngle))
        }
        //if(PoseLandmark.LEFT_ANKLE <= PoseLandmark.LEFT_FOOT_INDEX) return false
        return score
    }

    override fun getGrade(score: Double): String {
        var size = targetPose.targets.size

        return when {
            size * 5 >= score -> "Excellent!"
            size * 10 >= score -> "Great!"
            size * 25 >= score -> "Good!"
            else -> "Bad!"
        }
    }
}