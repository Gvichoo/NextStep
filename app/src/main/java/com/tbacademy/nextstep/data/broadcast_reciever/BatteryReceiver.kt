package com.tbacademy.nextstep.data.broadcast_reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager

class BatteryReceiver(
    private val onLowBattery: () -> Unit
) : BroadcastReceiver() {

    private var triggered = false

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BATTERY_CHANGED) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)

            if (level in 1..15 && !triggered) {
                triggered = true
                onLowBattery()
            }

            if (level > 15) {
                triggered = false // Reset if battery goes back up
            }
        }
    }
}