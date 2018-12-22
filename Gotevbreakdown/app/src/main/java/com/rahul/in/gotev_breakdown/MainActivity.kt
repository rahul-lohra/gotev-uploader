package com.rahul.`in`.gotev_breakdown

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import net.gotev.uploadservice.UploadNotificationConfig


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uploadManually()
    }

    fun uploadManually() {
        val url = ""
        val uploader = Uploader()
        val mConnection = uploader.prepareConnection(url, "PUT")
//        uploader.getResponse(mConnection,)
    }

    fun uploadMultipart(context: Context) {
        try {
            val uploadRequest = FileUploadRequest(context, "http://upload.server.com/path")
                .addFileToUpload("/absolute/path/to/your/file", "your-param-name")
                .setNotificationConfig(UploadNotificationConfig())
                .setMaxRetries(2)

            val task = FileUploadTask()
            task.setupParams(uploadRequest.params)


            val url = ""
            val uploader = Uploader()
            val mConnection = uploader.prepareConnection(url, "PUT")
            uploader.getResponse(mConnection, task)
        } catch (exc: Exception) {
            Log.e("AndroidUploadService", exc.message, exc)
        }

    }
}
