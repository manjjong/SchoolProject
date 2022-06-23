package com.jm.schoolproject.mlkit.pose

import com.google.mlkit.vision.pose.PoseLandmark
import com.jm.schoolproject.mlkit.TargetPose
import com.jm.schoolproject.mlkit.TargetShape
import kotlin.math.abs

class LeftDumbbellCurlPoseMatcher : DumbbellCurlPoseMatcher() {
    override val hand: Int = 15

    override fun initTargetPose() {
        targetPose = TargetPose(listOf(
            TargetShape(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_ELBOW, PoseLandmark.LEFT_WRIST, 50.0, "왼쪽을 좀 더 당겨주세요.", "왼쪽을 덜 당겨주세요."),
        ))
    }
    override fun initCountPose() {
        countPose = TargetPose(listOf(
            TargetShape(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_ELBOW, PoseLandmark.LEFT_WRIST, 150.0, "", ""),
        ))
    }

    override fun checkElbow(allPoseLandmark: ArrayList<PoseLandmark>): Double {
        var elbow = abs(allPoseLandmark[13].position.x - allPoseLandmark[11].position.x)
        return elbow.toDouble()
    }
}