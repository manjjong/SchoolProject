package com.jm.schoolproject.mlkit.pose

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import com.jm.schoolproject.FeedbackPose
import com.jm.schoolproject.mlkit.TargetPose
import com.jm.schoolproject.mlkit.TargetShape
import kotlin.math.abs
import kotlin.math.atan2

abstract class PoseMatcher {
    protected lateinit var countPose : TargetPose
    protected lateinit var targetPose : TargetPose

    open var offset = 15.0
    open var start = false

    init {
        initTargetPose()
        initCountPose()
    }

    abstract fun matchWithScore(pose : Pose) : Pair<Double, Int>
    abstract fun matchWithFeedback(pose : Pose, bitmap : Bitmap) : Pair<FeedbackPose?, Int>
    abstract fun initTargetPose()
    abstract fun initCountPose()
    abstract fun getGrade(score : Double) : String

    open fun extractLandmark(
        pose: Pose,
        target: TargetShape
    ): Triple<PoseLandmark?, PoseLandmark?, PoseLandmark?> {
        return Triple(
            extractLandmarkFromType(pose, target.firstLandmarkType),
            extractLandmarkFromType(pose, target.middleLandmarkType),
            extractLandmarkFromType(pose, target.lastLandmarkType)
        )
    }

    protected fun match(pose: Pose, targetPose: TargetPose): Boolean {
        targetPose.targets.forEach { target ->
            val (firstLandmark, middleLandmark, lastLandmark) = extractLandmark(pose, target)
            if (landmarkNotFound(firstLandmark, middleLandmark, lastLandmark)) {
                return false
            }

            val angle = calculateAngle(firstLandmark!!, middleLandmark!!, lastLandmark!!)
            val targetAngle = target.angle
            Log.d("pose", "${translate(firstLandmark!!.landmarkType)}, ${translate(middleLandmark!!.landmarkType)}, ${translate(lastLandmark!!.landmarkType)}")
            Log.d("pose", "angle : $angle -> target : $targetAngle")

            if (abs(angle - targetAngle) > offset) {
                return false
            }
        }
        return true
    }

    protected fun match(pose: ArrayList<PoseLandmark>, targetPose: TargetPose): Boolean {
        targetPose.targets.forEach { target ->
            val (firstLandmark, middleLandmark, lastLandmark) = Triple(pose[target.firstLandmarkType], pose[target.middleLandmarkType], pose[target.lastLandmarkType])
            if (landmarkNotFound(firstLandmark, middleLandmark, lastLandmark)) {
                return false
            }
            val angle = calculateAngle(firstLandmark, middleLandmark, lastLandmark)
            val targetAngle = target.angle

            Log.d("Test", "targetAngle : " + target.angle + ", angle : " + angle)
            if (abs(angle - targetAngle) > offset) {
                return false
            }
        }
        return true
    }

    open fun extractLandmarkFromType(pose: Pose, landmarkType: Int): PoseLandmark? {
        return pose.getPoseLandmark((landmarkType))
    }

    open fun landmarkNotFound(
        firstLandmark: PoseLandmark?,
        middleLandmark: PoseLandmark?,
        lastLandmark: PoseLandmark?
    ): Boolean {
        return firstLandmark == null || middleLandmark == null || lastLandmark == null
    }

    open fun calculateAngle(
        firstLandmark: PoseLandmark,
        middleLandmark: PoseLandmark,
        lastLandmark: PoseLandmark
    ): Double {
        val angle = Math.toDegrees(
            (atan2(
                lastLandmark.position.y - middleLandmark.position.y,
                lastLandmark.position.x - middleLandmark.position.x
            ) - atan2(
                firstLandmark.position.y - middleLandmark.position.y,
                firstLandmark.position.x - middleLandmark.position.x
            )).toDouble()
        )
        var absoluteAngle = abs(angle)
        if (absoluteAngle > 180) {
            absoluteAngle = 360 - absoluteAngle
        }
        return absoluteAngle
    }

    open fun anglesMatch(angle: Double, targetAngle: Double): Boolean {
        return angle < targetAngle + offset && angle > targetAngle - offset
    }

    companion object {
        val LEFT_STATE = 0
        val RIGHT_STATE = 1

        fun translate(pose: Int) : String {
            when (pose) {
                PoseLandmark.NOSE -> return "NOSE"
                PoseLandmark.LEFT_EYE_INNER -> return "LEFT_EYE_INNER"
                PoseLandmark.LEFT_EYE -> return "LEFT_EYE"
                PoseLandmark.LEFT_EYE_OUTER -> return "LEFT_EYE_OUTER"
                PoseLandmark.RIGHT_EYE_INNER -> return "RIGHT_EYE_INNER"
                PoseLandmark.RIGHT_EYE -> return "RIGHT_EYE"
                PoseLandmark.RIGHT_EYE_OUTER -> return "RIGHT_EYE_OUTER"
                PoseLandmark.LEFT_EAR -> return "LEFT_EAR"
                PoseLandmark.RIGHT_EAR -> return "RIGHT_EAR"
                PoseLandmark.LEFT_MOUTH -> return "LEFT_MOUTH"
                PoseLandmark.RIGHT_MOUTH -> return "RIGHT_MOUTH"
                PoseLandmark.LEFT_SHOULDER -> return "LEFT_SHOULDER"
                PoseLandmark.RIGHT_SHOULDER -> return "RIGHT_SHOULDER"
                PoseLandmark.LEFT_ELBOW -> return "LEFT_ELBOW"
                PoseLandmark.RIGHT_ELBOW -> return "RIGHT_ELBOW"
                PoseLandmark.LEFT_WRIST -> return "LEFT_WRIST"
                PoseLandmark.RIGHT_WRIST -> return "RIGHT_WRIST"
                PoseLandmark.LEFT_PINKY -> return "LEFT_PINKY"
                PoseLandmark.RIGHT_PINKY -> return "RIGHT_PINKY"
                PoseLandmark.LEFT_INDEX-> return "LEFT_INDEX"
                PoseLandmark.RIGHT_INDEX -> return "RIGHT_INDEX"
                PoseLandmark.LEFT_THUMB -> return "LEFT_THUMB"
                PoseLandmark.RIGHT_THUMB -> return "RIGHT_THUMB"
                PoseLandmark.LEFT_HIP -> return "LEFT_HIP"
                PoseLandmark.RIGHT_HIP -> return "RIGHT_HIP"
                PoseLandmark.LEFT_KNEE -> return "LEFT_KNEE"
                PoseLandmark.RIGHT_KNEE -> return "RIGHT_KNEE"
                PoseLandmark.LEFT_ANKLE -> return "LEFT_ANKLE"
                PoseLandmark.RIGHT_ANKLE -> return "RIGHT_ANKLE"
                PoseLandmark.LEFT_HEEL -> return "LEFT_HEEL"
                PoseLandmark.RIGHT_HEEL -> return "RIGHT_HEEL"
                PoseLandmark.LEFT_FOOT_INDEX -> return "LEFT_FOOT_INDEX"
                PoseLandmark.RIGHT_FOOT_INDEX -> return "RIGHT_FOOT_INDEX"
            }
            return ""
        }
    }
}