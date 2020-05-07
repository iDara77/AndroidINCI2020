package com.obsoft.inci2019.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


object RemoteServices {


    fun get(uri:String, handler: RemoteServicesHandler, callerId: Int, handlerId: Int) {
        this.execute("GET",uri, handler, callerId,handlerId)
    }

    fun post(uri:String, data: Map<String, String>?, handler: RemoteServicesHandler, callerId: Int,handlerId: Int) {
        this.execute("POST",uri, handler, callerId,handlerId,data)

    }

    fun put(uri:String, data: Map<String, String>?, handler: RemoteServicesHandler, callerId: Int,handlerId: Int) {
        this.execute("PUT",uri, handler, callerId,handlerId,data)

    }

    fun delete(uri:String, data: Map<String, String>?, handler: RemoteServicesHandler, callerId: Int,handlerId: Int) {
        this.execute("DELETE",uri, handler, callerId,handlerId,data)
    }

    fun getImage(uri: String, imgView: ImageView) {
        DownloadImageTask(imgView).execute(uri)
    }

    private fun execute(method:String, uri: String, handler: RemoteServicesHandler, callerId: Int,handlerId:Int, data: Map<String, String>? = null) {
        val url = URL(uri)
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        urlConnection.setRequestMethod(method);
        urlConnection.doInput = true
        if(data != null) urlConnection.doOutput = true

        val task = RemoteAsyncTask(urlConnection, handler, callerId,handlerId, data)
        task.execute()
    }

}

class RemoteAsyncTask(c: HttpURLConnection, h: RemoteServicesHandler, i: Int, hid: Int, d: Map<String, String>? = null) : AsyncTask<String, String, String>() {
    private var cnx: HttpURLConnection = c
    private var data:Map<String, String>? = d
    private var handler: RemoteServicesHandler = h
    private var callerId: Int = i
    private var handlerId: Int = hid

    override fun doInBackground(vararg p0: String?): String {
        cnx.connect()
        if (cnx.doOutput && data != null) {
            val os: BufferedOutputStream = BufferedOutputStream(cnx.getOutputStream())
            writeStream(os, data!!)
            os.close()
        }
        try {
            val streamIn: BufferedInputStream = BufferedInputStream(cnx.getInputStream())
            val response: String = readStream(inputStream = streamIn)
            handler.onFinishLoading(response, callerId, handlerId)
        } finally {
            cnx.disconnect()
        }
        return ""

    }
    fun readStream(inputStream: BufferedInputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        bufferedReader.forEachLine { stringBuilder.append(it) }
        return stringBuilder.toString()
    }
    fun writeStream(outputStream: BufferedOutputStream, data: Map<String, String>) {
        val bufferedWriter = BufferedWriter(OutputStreamWriter(outputStream))
        var p : List<String> = listOf()
        for (d in data) {
            p += d.key+"="+d.value
        }
        val sp = p.joinToString("&")
        bufferedWriter.write(sp)
        bufferedWriter.flush()
        bufferedWriter.close()
    }

}

interface RemoteServicesHandler {
    val context: Context?
    fun onFinishLoading(data: String, callerId: Int, handlerId: Int)
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

