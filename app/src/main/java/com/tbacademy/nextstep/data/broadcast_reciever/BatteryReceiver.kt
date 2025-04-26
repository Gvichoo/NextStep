package com.tbacademy.nextstep.data.broadcast_reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager

class BatteryReceiver(private val onLowBattery: () -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: return
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

        val batteryPct = level * 100 / scale.toFloat()

        if (batteryPct <= 15) {
            onLowBattery() // Callback to tell your app to change theme
        }
    }
}
