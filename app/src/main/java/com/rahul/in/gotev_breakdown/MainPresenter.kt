package com.rahul.`in`.gotev_breakdown

import android.content.Context
import android.util.Log
import java.io.File

class MainPresenter {

    val BASE_URL = "http://localhost:3002/"
    val UPLOAD_URL = BASE_URL + "upload"

    fun uploadFile(context: Context, file: File):Int {
        try {
            val uploadRequest = FileUploadRequest(context, UPLOAD_URL)
                .addFileToUpload(file.absolutePath, "avatar")

            val task = FileUploadTask()
            task.setupParams(uploadRequest.params)
            task.initVars()
            return task.forceUpload()

        } catch (exc: Exception) {
            println(exc.printStackTrace())
        }
        return 500
    }
}