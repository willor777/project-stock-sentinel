package com.willor.lib_data.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.willor.lib_data.domain.usecases.SaveTriggerUsecase


val gson = Gson()


fun logException(tag: String, exception: Exception, extraInfo: String? = null){

    val msg = if (extraInfo != null){
        "$extraInfo -- Exception: ${exception}\nStacktrace: ${exception.stackTraceToString()}"
    }else{
        "Exception: ${exception}\nStacktrace: ${exception.stackTraceToString()}"
    }

    Log.w(tag, msg)
}

suspend fun insertDummyTriggers(n: Int, saveTriggerUsecase: SaveTriggerUsecase) {
    var t = 0
    var p = 0.0
    for (i in 0..n){
        if (i % 2 == 0) {
            t = 1
            p = 23.0
        }else{
            t = -1
            p = 33.0
        }

        saveTriggerUsecase(
            "AAPL",
            "Test Strategy",
            "Description Goes Here Description Goes Here" +
                    " Description Goes Here Description Goes Here Description Goes Here",
            t,
            p,
            2333.04,
            2334.11,
            2344.22,
            false,
            false,
            System.currentTimeMillis()
        )
    }
}
