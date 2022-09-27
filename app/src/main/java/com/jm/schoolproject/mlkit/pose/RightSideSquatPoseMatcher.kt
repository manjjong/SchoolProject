package com.jm.schoolproject.mlkit.pose

import com.google.mlkit.vision.pose.PoseLandmark
import com.jm.schoolproject.mlkit.TargetPose
import com.jm.schoolproject.mlkit.TargetShape

class RightSideSquatPoseMatcher : SideSquatPoseMatcher() {
    override val leg: Int = 24

    override fun initTargetPose() {
        targetPose = TargetPose(listOf(
            TargetShape(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_ANKLE, 170.0, "왼쪽 무릎을 덜 펴주세요.", "기준보다 왼쪽 무릎이 구부려져있습니다. 왼쪽 다리를 펴주세요."),
            TargetShape(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_ANKLE, 85.0, "기준보다 오른쪽 무릎이 펴져있습니다. 오른쪽 무릎을 직각만큼 구부려주세요.", "기준보다 오른쪽 무릎이 구부려져있습니다. 오른쪽 무릎을 직각만큼만 구부려주세요."),
        ))
    }
    override fun initCountPose() {
        countPose = TargetPose(listOf(
            TargetShape(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_ANKLE, 170.0, "b", "s"),
            TargetShape(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE, 150.0, "b", "s"),
            TargetShape(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_ANKLE, 170.0, "b", "s"),
            TargetShape(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE, 150.0, "b", "s"),
        ))
    }
}