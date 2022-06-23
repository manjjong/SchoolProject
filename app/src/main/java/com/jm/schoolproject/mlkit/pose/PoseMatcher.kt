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
}