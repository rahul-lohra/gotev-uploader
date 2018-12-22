package com.rahul.`in`.gotev_breakdown

import net.gotev.uploadservice.NameValue
import net.gotev.uploadservice.http.HttpConnection
import net.gotev.uploadservice.http.impl.HurlBodyWriter
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class Uploader {

    val connectTimeout = 0
    val readTimeout = 0
    val useCaches = false
    val followRedirects = false

    fun prepareConnection(url: String, method: String) :HttpURLConnection{
        var mConnection: HttpURLConnection? = null
        val urlObj = URL(url)

        if (urlObj.protocol == "https") {
            mConnection = urlObj.openConnection() as HttpsURLConnection
        } else {
            mConnection = urlObj.openConnection() as HttpURLConnection
        }

        mConnection.doInput = true
        mConnection.doOutput = true
        mConnection.connectTimeout = connectTimeout
        mConnection.readTimeout = readTimeout
        mConnection.useCaches = useCaches
        mConnection.instanceFollowRedirects = followRedirects
        mConnection.requestMethod = method

        return mConnection
    }

    fun setHeaders(mConnection: HttpURLConnection, requestHeaders: List<NameValue>) {
        for (param in requestHeaders) {
            mConnection.setRequestProperty(param.name, param.value)
        }
    }


    @Throws(IOException::class)
    fun getResponse(mConnection: HttpURLConnection, delegate: FileUploadTask): Int {
        val bodyWriter = HurlBodyWriter(mConnection.outputStream)
        delegate.onBodyReady(bodyWriter)
        bodyWriter.flush()
        return mConnection.responseCode
    }
}