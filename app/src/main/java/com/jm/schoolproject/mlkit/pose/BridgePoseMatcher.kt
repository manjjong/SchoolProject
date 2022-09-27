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

class BridgePoseMatcher : PoseMatcher() {
    override fun initTargetPose() {
        targetPose = TargetPose(listOf(
            TargetShape(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE, 165.0, "기준에 비해 배를 더 내밀었습니다. 배를 평평하게 펴주세요.", "기준에 비해 배를 덜 내미셨습니다. 배를 평평하게 펴주세요."),
            TargetShape(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_ANKLE, 80.0, "기준에 비해 무릎이 더 펴져있습니다. 무릎을 좀 더 구부려주세요.", "기준에 비해 무릎이 더 구부려져있습니다. 무릎을 좀 더 펴주세요."),
            TargetShape(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE, 165.0, "기준에 비해 배를 더 내밀었습니다. 배를 평평하게 펴주세요.", "기준에 비해 배를 덜 내미셨습니다. 배를 평평하게 펴주세요."),
            TargetShape(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_ANKLE, 80.0, "기준에 비해 무릎이 더 펴져있습니다. 무릎을 좀 더 구부려주세요.", "기준에 비해 무릎이 더 구부려져있습니다. 무릎을 좀 더 펴주세요."),
        ))
    }
    override fun initCountPose() {
        countPose = TargetPose(listOf(
            TargetShape(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE, 125.0, "", ""),
            TargetShape(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_ANKLE, 53.0, "", ""),
            TargetShape(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE, 125.0, "", ""),
            TargetShape(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_ANKLE, 53.0, "", ""),
        ))
    }

    private val allPoseLandmark = ArrayList<PoseLandmark>()
    private lateinit var bitmap : Bitmap
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
                if (allPoseLandmark[24].position.y > pose.allPoseLandmarks[24].position.y ||
                    allPoseLandmark[23].position.y > pose.allPoseLandmarks[23].position.y) {
                    allPoseLandmark.clear()
                    allPoseLandmark.addAll(pose.allPoseLandmarks)
                    Log.d("Test", "엉덩이 높이 : " + allPoseLandmark[23].position.y + ", " + allPoseLandmark[24].position.y)
                }
            }
        }

        if(match(pose, countPose)) {
            Log.d("CameraX", "count 자세")
            if(allPoseLandmark.isNotEmpty()) {
                var score = countAndBridgeMatch(allPoseLandmark, targetPose)
                allPoseLandmark.clear()
                Log.d("CameraX", "score : " + score)
                return Pair(score, 1)
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
                if (allPoseLandmark[24].position.y > pose.allPoseLandmarks[24].position.y ||
                    allPoseLandmark[23].position.y > pose.allPoseLandmarks[23].position.y) {
                    allPoseLandmark.clear()
                    allPoseLandmark.addAll(pose.allPoseLandmarks)
                    this.bitmap = bitmap
                    Log.d("Squat", "allPoseLandmark : " + allPoseLandmark[24].position3D.y + ", pose : " + pose.allPoseLandmarks[24].position3D.y)
                }
            }
        }

        if (match(pose, countPose)) {
            if(allPoseLandmark.isNotEmpty()) {
                var fb = feedbackBridge(allPoseLandmark, targetPose)
                if (fb != null) {
                    return Pair(FeedbackPose(Feedback.translate(allPoseLandmark), fb, SerialBitmap.translate(this.bitmap)), 1)
                }
                allPoseLandmark.clear()
            }
        }
        return Pair(null, -1)
    }

    private fun feedbackBridge(allPoseLandmark: ArrayList<PoseLandmark>, targetPose: TargetPose) : List<TargetShape>? {
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

    private fun countAndBridgeMatch(allPoseLandmark: ArrayList<PoseLandmark>, targetPose: TargetPose): Double {
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
            Log.d("CameraX", "OffSet Over : " + target.angle + "->" + angle + ", " +
                    "${translate(firstLandmark.landmarkType)} - ${translate(middleLandmark.landmarkType)} -" +
                    "${translate(lastLandmark.landmarkType)}")

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