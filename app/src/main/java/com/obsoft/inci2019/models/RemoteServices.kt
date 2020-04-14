package com.obsoft.inci2019.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


object RemoteServices {


    fun get(uri:String, handler: RemoteServicesHandler, callerId: Int) {
        this.execute(uri, handler, callerId)
    }

    fun post() {

    }

    fun put() {

    }

    fun delete() {

    }

    fun getImage(uri: String, imgView: ImageView) {
        DownloadImageTask(imgView).execute(uri)
    }

    private fun execute(uri: String, handler: RemoteServicesHandler, callerId: Int) {
        val task = RemoteAsyncTask(uri, handler, callerId)
        task.execute()
    }

}

class RemoteAsyncTask(s: String, h: RemoteServicesHandler, i: Int) : AsyncTask<String, String, String>() {
    private var uri: String = s
    private var handler: RemoteServicesHandler = h
    private var callerId: Int = i

    override fun doInBackground(vararg p0: String?): String {
        val url = URL(uri)
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        try {
            val streamIn: BufferedInputStream = BufferedInputStream(urlConnection.getInputStream())
            val data: String = readStream(inputStream = streamIn)
            handler.onFinishLoading(data, callerId)
            return data
        } finally {
            urlConnection.disconnect()
        }
        return ""

    }
    fun readStream(inputStream: BufferedInputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        bufferedReader.forEachLine { stringBuilder.append(it) }
        return stringBuilder.toString()
    }

}

interface RemoteServicesHandler {
    val context: Context?
    fun onFinishLoading(data: String, callerId: Int)
}

private class DownloadImageTask(bmImage: ImageView) : AsyncTask<String?, Void?, Bitmap?>() {
    var bmImage: ImageView
    protected override fun doInBackground(vararg urls: String?): Bitmap? {
        val urldisplay = urls[0]
        var bmp: Bitmap? = null
        try {
            val `in`: InputStream = URL(urldisplay).openStream()
            bmp = BitmapFactory.decodeStream(`in`)
        } catch (e: Exception) {
            Log.e("Error", e.message)
            e.printStackTrace()
        }
        return bmp
    }

    override fun onPostExecute(result: Bitmap?) {
        bmImage.setImageBitmap(result)
    }

    init {
        this.bmImage = bmImage
    }
}

