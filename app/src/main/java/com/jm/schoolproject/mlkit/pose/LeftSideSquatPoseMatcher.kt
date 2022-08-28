package com.jm.schoolproject.mlkit.pose

import com.google.mlkit.vision.pose.PoseLandmark
import com.jm.schoolproject.mlkit.TargetPose
import com.jm.schoolproject.mlkit.TargetShape

class LeftSideSquatPoseMatcher : SideSquatPoseMatcher() {
    override val leg: Int = 25

    override fun initTargetPose() {
        targetPose = TargetPose(listOf(
            TargetShape(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_ANKLE, 85.0, "왼쪽 무릎을 조금 더 굽혀주세요.", "왼쪽 무릎을 조금 더 펴주세요."),
            TargetShape(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_ANKLE, 180.0, "오른쪽 무릎을 조금 더 굽혀 주세요.", "오른쪽 무릎을 조금 더 펴주세요."),
        ))
    }
    override fun initCountPose() {
        countPose = TargetPose(listOf(
            TargetShape(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_ANKLE, 180.0, "b", "s"),
            TargetShape(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE, 140.0, "b", "s"),
            TargetShape(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_ANKLE, 180.0, "b", "s"),
            TargetShape(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE, 140.0, "b", "s"),
        ))
    }
}