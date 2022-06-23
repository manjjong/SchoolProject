package com.jm.schoolproject.mlkit.pose

import com.google.mlkit.vision.pose.PoseLandmark
import com.jm.schoolproject.mlkit.TargetPose
import com.jm.schoolproject.mlkit.TargetShape
import kotlin.math.abs

class RightDumbbellCurlPoseMatcher : DumbbellCurlPoseMatcher() {

    override val hand: Int = 16

    override fun initTargetPose() {
        targetPose = TargetPose(listOf(
            TargetShape(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_WRIST, 50.0, "오른쪽을 좀 더 당겨주세요.", "오른쪽을 덜 당겨주세요."),
        ))
    }
    override fun initCountPose() {
        countPose = TargetPose(listOf(
            TargetShape(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_WRIST, 150.0, "", ""),
        ))
    }

    override fun checkElbow(allPoseLandmark: ArrayList<PoseLandmark>): Double {
        var elbow = abs(allPoseLandmark[14].position.x - allPoseLandmark[12].position.x)
        return elbow.toDouble()
    }
}