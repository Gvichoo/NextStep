package com.tbacademy.nextstep.presentation

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.data.broadcast_reciever.BatteryReceiver
import com.tbacademy.nextstep.databinding.ActivityMainBinding
import com.tbacademy.nextstep.domain.usecase.theme.AutoSwitchDarkThemeIfBatteryLowUseCase
import com.tbacademy.nextstep.presentation.manager.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var batteryReceiver: BatteryReceiver

    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 101

    @Inject
    lateinit var autoDarkModeUseCase: AutoSwitchDarkThemeIfBatteryLowUseCase

    @Inject
    lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setScreenSettings()
        setUpNotificationPermission()
        setUpBatteryReceiver()
        themeManager.observeAndApplyTheme()

        setContentView(binding.root)
    }


    private fun setUpBatteryReceiver() {
        batteryReceiver = BatteryReceiver {
            CoroutineScope(Dispatchers.Main).launch {
                val themeWasChanged = autoDarkModeUseCase()

                if (themeWasChanged) {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.battery_low_switched_to_dark_mode),
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
        }

        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryReceiver, filter)
    }

    private fun setUpNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted, request permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            NOTIFICATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Notification Permission", "Granted")
                } else {
                    // Permission denied, inform the user
                    Log.d("Notification Permission", "Denied")
                }
            }
        }
    }

    private fun setScreenSettings() {
        // Edge to Edge Padding
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                view.paddingLeft,
                systemBarsInsets.top,
                view.paddingRight,
                0
            )

            WindowInsetsCompat.CONSUMED
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)


        // Top Bar Color
        val isDarkMode =
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = !isDarkMode
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
    }

}