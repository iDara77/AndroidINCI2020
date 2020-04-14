package com.obsoft.inci2019

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Camera
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.obsoft.inci2019.models.CameraServices
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {

    lateinit var imageView : ImageView
    lateinit var videoView : VideoView
    lateinit var camSurfaceView : SurfaceView
    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Method #1
        imageView = findViewById<ImageView>(R.id.profile_image)
        videoView = findViewById<VideoView>(R.id.profile_video)

        // Method #2
        camSurfaceView = findViewById<SurfaceView>(R.id.cam_surface)
        CameraServices.loadCamSurface(this, camSurfaceView!!)
    }

    fun showCamImage(id: Int) {
        imageView.isVisible = id == 0
        videoView.isVisible = id == 1
        camSurfaceView.isVisible = id == 2
    }


    // Method #1
    fun takeAPicButtonPressed(view:View) {
        dispatchTakePictureIntent()
    }
    fun takeAVidButtonPressed(view:View) {
        dispatchTakeVideoIntent()
    }

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_VIDEO_CAPTURE = 2

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }

                // Continue only if the File was successfully created
                photoFile?.also {
                    try {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.obsoft.inci2019.fileprovider",
                            it
                        )

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }

                }
            }
        }
    }

    private fun dispatchTakeVideoIntent() {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            takeVideoIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if(data?.extras?.get("data") != null) {
                val imageBitmap = data?.extras?.get("data") as Bitmap
                imageView?.setImageBitmap(imageBitmap)
            }
            val f = File(currentPhotoPath)
//            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//            val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//            val f2 = File(storageDir, "JPEG_${timeStamp}_.jpg")
//            val fo = FileOutputStream(f2)
//            fo.write(f.readBytes())
//            fo.flush()
//            fo.close()
            imageView.setImageURI(FileProvider.getUriForFile(this, "com.obsoft.inci2019.fileprovider", f))
            showCamImage(0)
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            val videoUri: Uri? = data?.data
            videoView?.setVideoURI(videoUri)
            videoView?.start()
            showCamImage(1)
        }
    }

    // Method 2:
    fun openCamPressed(view:View) {
        CameraServices.loadCamSurface(this, camSurfaceView!!)
        showCamImage(2)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
}
