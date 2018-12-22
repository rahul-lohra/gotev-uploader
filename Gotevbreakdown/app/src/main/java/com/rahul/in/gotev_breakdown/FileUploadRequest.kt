package com.rahul.`in`.gotev_breakdown

import android.content.Context
import android.content.Intent
import net.gotev.uploadservice.MultipartUploadRequest

class FileUploadRequest(context: Context?, serverUrl: String?) : MultipartUploadRequest(context, serverUrl) {

    override fun initializeIntent(intent: Intent?) {
        val task = FileUploadTask()
        task.setupParams(params)
        task.run()
    }
}