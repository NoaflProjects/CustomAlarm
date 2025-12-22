package com.android.customalarm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.customalarm.ui.alarms.AlarmsScreen
import com.android.customalarm.ui.alarmsetup.AlarmSetUpScreen

/** Navigation host for the Custom Alarm application. */
@Composable
fun CustomAlarmNavHost() {
  val navController = rememberNavController()

  NavHost(navController = navController, startDestination = Routes.ALARMS) {
    composable(Routes.ALARMS) {
      AlarmsScreen(onAddAlarm = { navController.navigate(Routes.ALARM_SETUP) })
    }

    composable(Routes.ALARM_SETUP) {
      AlarmSetUpScreen(
          onNavigateBack = { navController.popBackStack() },
          onSaveAlarm = { navController.navigate(Routes.ALARMS) })
    }
  }
}
