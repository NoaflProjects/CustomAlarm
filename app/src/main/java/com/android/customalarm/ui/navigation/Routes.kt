package com.android.customalarm.ui.navigation

/** Routes used in the Custom Alarm application for navigation. */
object Routes {
  const val ALARMS = "alarms"

  // Route for adding a new alarm
  const val ALARM_SETUP = "alarm_setup"

  // Route for editing an existing alarm with a specific ID
  fun alarmSetup(alarmId: String) = "${ALARM_SETUP}/$alarmId"
}
