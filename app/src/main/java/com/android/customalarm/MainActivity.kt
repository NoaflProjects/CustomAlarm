package com.android.customalarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.android.customalarm.ui.navigation.CustomAlarmNavHost
import com.android.customalarm.ui.theme.CustomAlarmTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent { CustomAlarmTheme { CustomAlarmNavHost() } }
  }
}
