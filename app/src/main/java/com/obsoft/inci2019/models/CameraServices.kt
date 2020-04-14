package com.obsoft.inci2019.models

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.util.Log
import android.view.Surface
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import java.util.*


object CameraServices {
    private const val CAMERA_IMAGE: Long = 1
    private const val CAMERA_VIDEO: Long = 2
    private const val TAG: String = "CameraServices"
    private var surfaceView: SurfaceView? = null

    fun checkPermissions(activity: AppCompatActivity): Boolean {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    fun requestPermissions(activity: AppCompatActivity, permissionId: Int) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.CAMERA),
            permissionId
        )
    }

    fun loadCamSurface(activity: AppCompatActivity, surfaceView: SurfaceView) {
        if (CameraServices.checkPermissions(activity)) {
            this.surfaceView = surfaceView
            initCamera(activity)
        } else {
            CameraServices.requestPermissions(activity, 1)
        }
    }

    @SuppressLint("MissingPermission")
    private fun initCamera(activity: AppCompatActivity) {
        Log.d(TAG, "initCamera() start")
        val cameraManager =
            activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager?
        Log.d(TAG, "acquired cameraManager: $cameraManager")
        val cameraIdList: Array<String>
        cameraIdList = try {
            cameraManager!!.cameraIdList
        } catch (e: CameraAccessException) {
            Log.e(TAG, "couldn't get camera list", e)
            return
        }
        Log.d(TAG, "acquired cameraIdList: length: " + cameraIdList.size)
        if (cameraIdList.size == 0) {
            Log.w(TAG, "couldn't detect a camera")
            return
        }
        val camera0Id = cameraIdList[0]
        Log.d(TAG, "chosen camera: $camera0Id")
        try {
            cameraManager.openCamera(camera0Id, deviceCallback, null)
        } catch (e: CameraAccessException) {
            Log.e(TAG, "couldn't open camera", e)
        }
        Log.d(TAG, "called cameraManager.openCamera()")
    }

    var deviceCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            Log.d(TAG, "deviceCallback.onOpened() start")
            val surface: Surface = surfaceView?.getHolder()!!.getSurface()
            Log.d(TAG, "surface: $surface")
            val surfaceList: List<Surface> = Collections.singletonList(surface)
            try {
                camera.createCaptureSession(surfaceList, sessionCallback, null)
            } catch (e: CameraAccessException) {
                Log.e(TAG, "couldn't create capture session for camera: " + camera.id, e)
                return
            }
        }

        override fun onDisconnected(camera: CameraDevice) {
            Log.d(TAG, "deviceCallback.onDisconnected() start")
        }

        override fun onError(camera: CameraDevice, error: Int) {
            Log.d(TAG, "deviceCallback.onError() start")
        }
    }

    var sessionCallback: CameraCaptureSession.StateCallback =
        object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                Log.i(TAG, "capture session configured: $session")
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                Log.e(TAG, "capture session configure failed: $session")
            }
        }
}