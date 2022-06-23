package com.jm.schoolproject.mlkit

import java.io.Serializable

class TargetShape(
    val firstLandmarkType: Int,
    val middleLandmarkType: Int,
    val lastLandmarkType: Int,
    val angle : Double,
    val bigMessage : String,
    val smallMessage : String
) : Serializable, Cloneable {
    private var targetAngle = 0.0

    fun setTargetAngle(angle : Double) {
        targetAngle = angle
    }

    fun getMessage() : String {
        if (targetAngle > angle!!) return bigMessage
        return smallMessage
    }

    public override fun clone() : TargetShape {
        var tmp = TargetShape(firstLandmarkType, middleLandmarkType, lastLandmarkType, angle, bigMessage, smallMessage)
        tmp.targetAngle = targetAngle
        return tmp
    }
}