package com.rahul.`in`.gotev_breakdown


public interface ShotoUploadDelegate {
    fun onCompleted()
    fun onError(exception: Exception? = null)
}
