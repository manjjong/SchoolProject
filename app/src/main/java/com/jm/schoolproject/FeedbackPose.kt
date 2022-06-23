package com.jm.schoolproject

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.mlkit.vision.pose.PoseLandmark
import com.jm.schoolproject.mlkit.TargetShape
import java.io.ByteArrayOutputStream
import java.io.Serializable
import java.lang.Exception

data class FeedbackPose(
    var landmarks : List<CustomPoseLandmark>,
    var feedback : List<TargetShape>,
    var bitmapData: ByteArray
) : Serializable

data class CustomPoseLandmark(
    val i : Int,
    val coordinate: Coordinate
) : Serializable

data class Coordinate(
    val x : Float,
    val y : Float
) : Serializable

class SerialBitmap {
    companion object {
        fun translate(bitmap: Bitmap) : ByteArray {
            lateinit var bitmapData : ByteArray
            try {
                var stream: ByteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                bitmapData = stream.toByteArray()
                stream.flush()
                stream.close()
            } catch (e : Exception) {
                e.printStackTrace()
            }
            return bitmapData
        }
        fun translate(bitmapData : ByteArray) : Bitmap {
            return BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.size).copy(Bitmap.Config.ARGB_8888, true)
        }
    }
}

class Feedback {
    companion object {
        fun translate(list : ArrayList<PoseLandmark>) : ArrayList<CustomPoseLandmark> {
            var result : ArrayList<CustomPoseLandmark> = ArrayList<CustomPoseLandmark>()

            for (x in list) {
                result.add(CustomPoseLandmark(x.landmarkType, Coordinate(x.position.x, x.position.y)))
            }
            return result
        }
    }
}