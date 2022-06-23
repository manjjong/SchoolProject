package com.jm.schoolproject.mlkit.pose

import com.google.mlkit.vision.pose.PoseLandmark
import com.jm.schoolproject.mlkit.TargetPose
import com.jm.schoolproject.mlkit.TargetShape

class LeftLungePoseMatcher : LungePoseMatcher() {
    override fun initTargetPose() {
        targetPose = TargetPose(listOf(
            TargetShape(PoseLandmark.LEFT_ANKLE, PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_HIP, 65.0, "왼쪽 무릎을 좀더 구부려주세요.", "왼쪽 무릎을 좀더 펴주세요."),
            TargetShape(PoseLandmark.RIGHT_ANKLE, PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_HIP, 100.0, "오른쪽 무릎을 좀더 구부려주세요.", "오른쪽 무릎을 좀더 펴주세요."),
            TargetShape(PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_SHOULDER, 90.0, "상체를 뒤쪽으로 움직여 수직을 만들어주세요.", "상체를 앞쪽으로 움직여 수직을 만들어주세요."),
            TargetShape(PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_SHOULDER, 165.0, "상체를 뒤쪽으로 움직여 수직을 만들어주세요.", "상체를 앞쪽으로 움직여 수직을 만들어주세요."),
        ))
    }
    override fun initCountPose() {
        countPose = TargetPose(listOf(
            TargetShape(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_ANKLE, 155.0, "", ""),
            TargetShape(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_ANKLE, 145.0, "", ""),
            TargetShape(PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_SHOULDER, 135.0, "", ""),
            TargetShape(PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_SHOULDER, 160.0, "", ""),
        ))
    }
}