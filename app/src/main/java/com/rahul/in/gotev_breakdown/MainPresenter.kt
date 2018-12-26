package com.rahul.`in`.gotev_breakdown

import android.content.Context
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import java.io.File

class MainPresenter {

    val BASE_URL = "http://localhost:3002/"
    val UPLOAD_URL = BASE_URL + "upload"

    fun uploadFileLoop(context: Context, file: File): Int {
        val startTime = System.currentTimeMillis()
        var s = 0
        while (s < 40) {
            uploadFile(context, file)
            s += 1
        }

        val endTime = System.currentTimeMillis()
        println("FILE loop, sTime = $startTime, eTime = $endTime")
        return 200
    }

    fun uploadFileOkHttpLoop(context: Context, file: File): Int {
        val startTime = System.currentTimeMillis()
        var s = 0
        while (s < 40) {
            uploadFileOkHttp(context, file)
            s += 1
        }
        val endTime = System.currentTimeMillis()
        println("Okhttp loop, sTime = $startTime, eTime = $endTime")
        return 200
    }

    fun uploadFile(context: Context, file: File): Int {
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


    fun uploadFileOkHttp(context: Context, file: File): Int {

        fun getRequestBodyForMultipart(file: File): RequestBody {
            val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("avatar", "file.png", requestFile)
                .build()
            return requestBody
        }

        val fileUploadClient = OkHttpClient().newBuilder().build()

        val body = getRequestBodyForMultipart(file)
        val httpRequest = okhttp3.Request.Builder()
            .url(UPLOAD_URL)
            .post(body)
            .build()
        val response = fileUploadClient.newCall(httpRequest).execute()
        return response.code()
    }
}