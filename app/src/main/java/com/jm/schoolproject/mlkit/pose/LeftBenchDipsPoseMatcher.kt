package com.jm.schoolproject.mlkit.pose

import com.google.mlkit.vision.pose.PoseLandmark
import com.jm.schoolproject.mlkit.TargetPose
import com.jm.schoolproject.mlkit.TargetShape

class LeftBenchDipsPoseMatcher : BenchDipsPoseMatcher() {
    override val hip: Int = 23

    override fun initTargetPose() {
        targetPose = TargetPose(listOf(
            TargetShape(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_ELBOW, PoseLandmark.LEFT_ANKLE, 90.0, "허리를 좀더 숙여주세요.", "허리를 좀더 펴주세요."),
            TargetShape(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_ELBOW, 90.0, "왼쪽 무릎을 좀더 구부려주세요.", "왼쪽 무릎을 좀더 펴주세요."),
        ))
    }
    override fun initCountPose() {
        countPose = TargetPose(listOf(
            TargetShape(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_ELBOW, PoseLandmark.LEFT_ANKLE, 170.0, "", ""),
            TargetShape(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_ELBOW, 60.0, "", ""),
        ))
    }
}