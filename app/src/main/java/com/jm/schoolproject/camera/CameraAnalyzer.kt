package com.jm.schoolproject.camera

import android.annotation.SuppressLint
import android.graphics.*
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetector

import android.graphics.Bitmap
import android.media.Image
import android.util.Log
import com.google.mlkit.vision.common.internal.ImageConvertUtils

class CameraAnalyzer (
    private val poseDetector : PoseDetector,
    private val onPoseDetected: (pose: Pose, bitmap: Bitmap) -> Unit
) : ImageAnalysis.Analyzer {
    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val medialImage = imageProxy.image ?: return
        val inputImage = InputImage.fromMediaImage(medialImage, imageProxy.imageInfo.rotationDegrees)

        poseDetector.process(inputImage).addOnSuccessListener { pose ->
            onPoseDetected(pose, inputImage.toBitmap())
        }.addOnFailureListener { e ->

        }.addOnCompleteListener {
            imageProxy.close()
            medialImage.close()
        }
    }

    private fun InputImage.toBitmap(): Bitmap {
        return ImageConvertUtils.getInstance().getUpRightBitmap(this)
    }
}