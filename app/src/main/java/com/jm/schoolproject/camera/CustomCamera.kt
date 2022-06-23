package com.jm.schoolproject.camera

import android.util.Log
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CustomCamera (
    private val activity: AppCompatActivity,
    private val analyzer: ImageAnalysis.Analyzer
) {
    private var cameraExecutor : ExecutorService = Executors.newSingleThreadExecutor()
    private lateinit var preview : Preview
    private lateinit var imageAnalyzer : ImageAnalysis

    fun startCamera(surfaceProvider: Preview.SurfaceProvider) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)

        cameraProviderFuture.addListener({
            val cameraProvider : ProcessCameraProvider = cameraProviderFuture.get()

            this.preview = Preview.Builder().build().also {
                it.setSurfaceProvider(surfaceProvider)
            }

            this.imageAnalyzer = ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build().also {
                it.setAnalyzer(cameraExecutor, analyzer)
            }

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    activity,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )
            } catch(exc: Exception) {
                Log.d("CameraX-Debug", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(activity))
    }

    fun onDestroy() {
        cameraExecutor.shutdown()
    }
}