package com.android.customalarm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.customalarm.ui.alarms.AlarmsScreen
import com.android.customalarm.ui.alarmsetup.AlarmSetUpScreen

/** Navigation host for the Custom Alarm application. */
@Composable
fun CustomAlarmNavHost() {
  val navController = rememberNavController()

  NavHost(navController = navController, startDestination = Routes.ALARMS) {
    composable(Routes.ALARMS) {
      AlarmsScreen(
          onAddAlarm = { navController.navigate(Routes.ALARM_SETUP) },
          onClickAlarm = { alarmId -> navController.navigate(Routes.alarmSetupWithId(alarmId)) })
    }

    composable(Routes.ALARM_SETUP) {
      AlarmSetUpScreen(
          onNavigateBack = { navController.popBackStack() },
          onSaveAlarm = { navController.popBackStack() })
    }

    composable(
        route = "alarmSetup/{alarmId}",
        arguments = listOf(navArgument("alarmId") { type = NavType.StringType })) { backStackEntry
          ->
          val alarmId =
              backStackEntry.arguments?.getString("alarmId")
                  ?: run {
                    navController.popBackStack()
                    return@composable
                  }
          AlarmSetUpScreen(
              alarmId = alarmId,
              onNavigateBack = { navController.popBackStack() },
              onSaveAlarm = { navController.popBackStack() })
        }
  }
}
