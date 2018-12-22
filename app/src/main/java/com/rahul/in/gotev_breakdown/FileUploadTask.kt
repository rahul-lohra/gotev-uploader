package com.rahul.`in`.gotev_breakdown

import net.gotev.uploadservice.*
import net.gotev.uploadservice.BuildConfig
import net.gotev.uploadservice.http.BodyWriter
import net.gotev.uploadservice.http.impl.HurlStack
import java.io.IOException
import java.lang.Exception
import java.nio.charset.Charset

class FileUploadTask: MultipartUploadTask() {

    fun setupParams(uploadParams:UploadTaskParameters){
        params = uploadParams
    }

    @Throws(IOException::class)
    override fun onBodyReady(bodyWriter: BodyWriter) {
        //reset uploaded bytes when the body is ready to be written
        //because sometimes this gets invoked when network changes
        uploadedBytes = 0
        writeRequestParameters(bodyWriter)
        writeFiles(bodyWriter)
        bodyWriter.write(trailerBytes)
        uploadedBytes += trailerBytes.size.toLong()
    }

    override fun broadcastProgress(uploadedBytes: Long, totalBytes: Long) {

    }

    fun forceRun() = forceUpload()

    fun initVars(){
        val boundary = BOUNDARY_SIGNATURE + System.nanoTime()
        boundaryBytes = (TWO_HYPHENS + boundary + NEW_LINE).toByteArray(US_ASCII)
        trailerBytes = (TWO_HYPHENS + boundary + TWO_HYPHENS + NEW_LINE).toByteArray(US_ASCII)
        charset = Charset.forName("UTF-8")

        if (params.files.size <= 1) {
            httpParams.addHeader("Connection", "close")
        } else {
            httpParams.addHeader("Connection", "Keep-Alive")
        }

        httpParams.addHeader("Content-Type", "multipart/form-data; boundary=$boundary")
    }

    fun forceUpload():Int {

        try {
            successfullyUploadedFiles.clear()
            uploadedBytes = 0
            totalBytes = bodyLength

            if (httpParams.isCustomUserAgentDefined) {
                httpParams.addHeader("User-Agent", httpParams.customUserAgent)
            } else {
                httpParams.addHeader("User-Agent", "AndroidUploadService/" + BuildConfig.VERSION_NAME)
            }

            connection = HTTP_STACK
                .createNewConnection(httpParams.method, params.serverUrl)
                .setHeaders(httpParams.requestHeaders)
                .setTotalBodyBytes(totalBytes, httpParams.usesFixedLengthStreamingMode)

            val response = connection.getResponse(this)
            return response.httpCode
        } finally {
            if (connection != null)
                connection.close()
        }
    }

    companion object {
        val HTTP_STACK = HurlStack()
    }

}