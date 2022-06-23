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

abstract class DumbbellCurlPoseMatcher : PoseMatcher() {
    override fun initTargetPose() {
        targetPose = TargetPose(listOf(
            //TargetShape(PoseLandmark.LEFT_ELBOW, PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_HIP, 5.0, "왼쪽 팔꿈치를 좀더 옆구리에 붙여주세요.", "왼쪽 팔꿈치를 좀더 옆구리에 붙여주세요."),
            //TargetShape(PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_HIP, 5.0, "오른쪽 팔꿈치를 좀더 옆구리에 붙여주세요.", "오른쪽 팔꿈치를 좀더 옆구리에 붙여주세요."),
            //TargetShape(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_ELBOW, PoseLandmark.LEFT_WRIST, 50.0, "왼쪽을 좀 더 당겨주세요.", "왼쪽을 덜 당겨주세요."),
            TargetShape(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_WRIST, 50.0, "오른쪽을 좀 더 당겨주세요.", "오른쪽을 덜 당겨주세요."),
        ))
    }
    override fun initCountPose() {
        countPose = TargetPose(listOf(
            //TargetShape(PoseLandmark.LEFT_ELBOW, PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_HIP, 5.0, "", ""),
            //TargetShape(PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_HIP, 5.0, "", ""),
            //TargetShape(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_ELBOW, PoseLandmark.LEFT_WRIST, 150.0, "", ""),
            TargetShape(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_WRIST, 150.0, "", ""),
        ))
    }

    private val allPoseLandmark = ArrayList<PoseLandmark>()
    private lateinit var bitmap : Bitmap
    abstract val hand : Int

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
                if (allPoseLandmark[hand].position.y > pose.allPoseLandmarks[hand].position.y) {
                    allPoseLandmark.clear()
                    allPoseLandmark.addAll(pose.allPoseLandmarks)
                    Log.d("bar", "${allPoseLandmark[12].position}, ${allPoseLandmark[14].position}, ${allPoseLandmark[16].position}")

                }
            }
        }

        if(match(pose, countPose)) {
            if(allPoseLandmark.isNotEmpty()) {
                var score = countAndBarbellCurlMatch(allPoseLandmark, targetPose)
                allPoseLandmark.clear()
                Log.d("CameraX", "score : " + score)
                if (score > 0) {
                    Log.d("test", "hi")
                }
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
                if (allPoseLandmark[hand].position.y > pose.allPoseLandmarks[hand].position.y) {
                    allPoseLandmark.clear()
                    allPoseLandmark.addAll(pose.allPoseLandmarks)
                    this.bitmap = bitmap
                }
            }
        }

        if (match(pose, countPose)) {
            Log.d("test", "${allPoseLandmark[14].position}, ${allPoseLandmark[12].position}, ${allPoseLandmark[24].position}")
            if(allPoseLandmark.isNotEmpty()) {
                var fb = feedbackBarbellCurl(allPoseLandmark, targetPose)
                if (fb != null) {
                    return Pair(FeedbackPose(Feedback.translate(allPoseLandmark), fb, SerialBitmap.translate(this.bitmap)), 1)
                }
                allPoseLandmark.clear()
            }
        }
        return Pair(null, -1)
    }

    private fun feedbackBarbellCurl(allPoseLandmark: ArrayList<PoseLandmark>, targetPose: TargetPose) : List<TargetShape>? {
        var feedback = ArrayList<TargetShape>()

        var elbow = checkElbow(allPoseLandmark)
        if (elbow > 50 || elbow < -50) return null
        if (elbow > 15 || elbow < -15) { feedback.add(TargetShape(PoseLandmark.LEFT_ELBOW, PoseLandmark.RIGHT_ELBOW, -1, 0.0, "팔꿈치를 옆구리에 붙이세요.", "팔꿈치를 옆구리에 붙이세요."))}

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

    private fun countAndBarbellCurlMatch(allPoseLandmark: ArrayList<PoseLandmark>, targetPose: TargetPose): Double {
        var score : Double = 0.0
        var elbow = checkElbow(allPoseLandmark)

        if (elbow > 15) return -1.0

        score += elbow

        targetPose.targets.forEach { target ->
            val (firstLandmark, middleLandmark, lastLandmark) = Triple(allPoseLandmark[target.firstLandmarkType],
                allPoseLandmark[target.middleLandmarkType],
                allPoseLandmark[target.lastLandmarkType]
            )

            if (landmarkNotFound(firstLandmark, middleLandmark, lastLandmark)) {
                Log.d("CameraX", "LandmarkNotFound : " + target.angle)
                return -1.0
            }

            val angle = calculateAngle(firstLandmark, middleLandmark, lastLandmark)
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

    abstract fun checkElbow(allPoseLandmark: ArrayList<PoseLandmark>) : Double

    override fun getGrade(score: Double): String {
        var size = targetPose.targets.size + 2

        return when {
            size * 5 >= score -> "Excellent!"
            size * 10 >= score -> "Great!"
            size * 25 >= score -> "Good!"
            else -> "Bad!"
        }
    }
}