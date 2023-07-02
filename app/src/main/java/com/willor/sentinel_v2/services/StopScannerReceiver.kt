package com.willor.sentinel_v2.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


/*
TODO
    - Don't forget to add to manifest.xml
    - When the user clicks "Stop Scanner" on the notification, This broadcast receiver will
    be called to stop the scanner and clear the notification.
 */
class StopScannerReceiver: BroadcastReceiver() {

    private val tag: String = StopScannerReceiver::class.java.simpleName

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(tag, "onReceive() Called!")
        context?.stopService(Intent(context, StockScannerService::class.java))
    }
}