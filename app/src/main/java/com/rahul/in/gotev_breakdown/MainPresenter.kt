package com.rahul.`in`.gotev_breakdown

import android.content.Context
import net.gotev.uploadservice.MultipartUploadRequest
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import java.io.File

class MainPresenter {

    val BASE_URL = "http://localhost:3002/"
    val UPLOAD_URL = BASE_URL + "upload"

    fun uploadFile(context: Context, file: File, shotoUploadDelegate: ShotoUploadDelegate? = null): Int {
        try {
            val uploadRequest = FileUploadRequest(context, UPLOAD_URL)
                .addFileToUpload(file.absolutePath, "avatar")
//            val uploadRequest = provideMultipartRequest(context, file)

            val task = FileUploadTask()
            task.setupParams(uploadRequest.params)
            task.initVars()
            val code = task.forceUpload()
            val success = (code in 200..299)
            if (success) {
                shotoUploadDelegate?.onCompleted()
            } else {
                shotoUploadDelegate?.onError(null)
            }
            return code

        } catch (exc: Exception) {
            shotoUploadDelegate?.onError(exc)
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

    fun provideMultipartRequest(context: Context, file: File): MultipartUploadRequest {
        val endPoint = "https://s3.ap-southeast-1.amazonaws.com/shoto-production-singapore"
        val key = "Users/5b8b9c488190d6026b00005b/Photos/5c4591ddbb1c2e7b11000393/thumbnail.jpg"
        val acl = "private"
        val contentType = "multipart/form-data"
        val xAmzCredential = "AKIAJTL7QV25G7RLJ2TQ/20190121/ap-southeast-1/s3/aws4_request"
        val xAmzAlgorithm = "AWS4-HMAC-SHA256"
        val xAmzDate = "20190121T114443Z"
        val policy = "eyJleHBpcmF0aW9uIjoiMjAxOS0wMS0yN1QxMTo0NDo0M1oiLCJjb25kaXRpb25zIjpbeyJidWNrZXQiOiJzaG90by1wcm9kdWN0aW9uLXNpbmdhcG9yZSJ9LFsic3RhcnRzLXdpdGgiLCIka2V5IiwiIl0seyJhY2wiOiJwcml2YXRlIn0sWyJzdGFydHMtd2l0aCIsIiRDb250ZW50LVR5cGUiLCIiXSx7IngtYW16LWFsZ29yaXRobSI6IkFXUzQtSE1BQy1TSEEyNTYifSx7IngtYW16LWNyZWRlbnRpYWwiOiJBS0lBSlRMN1FWMjVHN1JMSjJUUS8yMDE5MDEyMS9hcC1zb3V0aGVhc3QtMS9zMy9hd3M0X3JlcXVlc3QifSx7IngtYW16LWRhdGUiOiIyMDE5MDEyMVQxMTQ0NDNaIn0sWyJjb250ZW50LWxlbmd0aC1yYW5nZSIsMCw1MjQyODgwMDBdXX0="
        val xAmzSignature = "e00750f3702e867fd11247abb2c15ea050ff24447fba27a2f81a453e244a17a6"

        return MultipartUploadRequest(context, endPoint)
            .addFileToUpload(file.absolutePath, "file")
            .addParameter("key", key)
            .addParameter("acl", acl)
            .addParameter("Content-Type", contentType)
            .addParameter("X-Amz-Credential", xAmzCredential)
            .addParameter("X-Amz-Algorithm", xAmzAlgorithm)
            .addParameter("X-Amz-Date", xAmzDate)
            .addParameter("Policy", policy)
            .addParameter("X-Amz-Signature", xAmzSignature)
    }
}