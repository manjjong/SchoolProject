package com.jm.schoolproject.mlkit.pose

import com.google.mlkit.vision.pose.PoseLandmark
import com.jm.schoolproject.mlkit.TargetPose
import com.jm.schoolproject.mlkit.TargetShape

class RightBenchDipsPoseMatcher : BenchDipsPoseMatcher() {
    override val hip: Int = 24

    override fun initTargetPose() {
        targetPose = TargetPose(listOf(
            TargetShape(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_ANKLE, 90.0, "허리를 좀더 숙여주세요.",    "허리를 좀더 펴주세요."),
            TargetShape(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW, 90.0, "오른쪽 무릎을 좀더 구부려주세요.", "오른쪽 무릎을 좀더 펴주세요."),
        ))
    }
    override fun initCountPose() {
        countPose = TargetPose(listOf(
            TargetShape(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_ANKLE, 170.0, "", ""),
            TargetShape(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW, 60.0, "", ""),
        ))
    }
}