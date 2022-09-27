package com.jm.schoolproject.mlkit.pose

import com.google.mlkit.vision.pose.PoseLandmark
import com.jm.schoolproject.mlkit.TargetPose
import com.jm.schoolproject.mlkit.TargetShape

class RightBenchDipsPoseMatcher : BenchDipsPoseMatcher() {
    override val hip: Int = 24

    override fun initTargetPose() {
        targetPose = TargetPose(listOf(
            TargetShape(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_WRIST, 90.0, "팔이 기준보다 펴져있습니다. 팔을 직각 모양으로 구부려주세요.", "팔을 기준보다 구부렸습니다. 팔을 직각 모양으로 구부려주세요."),
            TargetShape(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW, 90.0, "엉덩이가 앞쪽으로 나와있습니다. 좀 더 직각으로 앉아주세요.", "엉덩이가 너무 뒤쪽으로 빠져있습니다. 좀 더 직각으로 앉아주세요."),
        ))
    }
    override fun initCountPose() {
        countPose = TargetPose(listOf(
            TargetShape(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_WRIST, 160.0, "", ""),
            TargetShape(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW, 30.0, "", ""),
        ))
    }
}