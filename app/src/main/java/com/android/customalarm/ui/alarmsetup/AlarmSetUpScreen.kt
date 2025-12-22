package com.android.customalarm.ui.alarmsetup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.android.customalarm.R
import com.android.customalarm.ui.common.TopBar
import com.android.customalarm.ui.theme.Dimens.paddingMedium

/** Test tags for AlarmSetUpScreen components */
object AlarmSetUpScreenTags {
  const val ROOT = "alarm_setup_screen_root"
  const val LEFT_TOPBAR_BUTTON = "alarm_setup_screen_left_topbar_button"
  const val MIDDLE_TOPBAR_TEXT = "alarm_setup_screen_middle_topbar_text"
  const val RIGHT_TOPBAR_BUTTON = "alarm_setup_screen_right_topbar_button"
  const val TIME_PICKER = "alarm_setup_screen_time_picker"
  const val HOUR_PICKER = "alarm_setup_screen_hour_picker"
  const val MINUTE_PICKER = "alarm_setup_screen_minute_picker"
  const val ALARM_NAME_FIELD = "alarm_setup_screen_alarm_name_field"
}

/**
 * Composable function for the Alarm Setup Screen.
 *
 * @param onNavigateBack Lambda function to be called when the back button is clicked.
 * @param onSaveAlarm Lambda function to be called when the save button is clicked.
 */
@Composable
fun AlarmSetUpScreen(onNavigateBack: () -> Unit = {}, onSaveAlarm: () -> Unit = {}) {

  // State for the selected time
  var hour by remember { mutableIntStateOf(12) }
  var minute by remember { mutableIntStateOf(0) }
  var alarmName by remember { mutableStateOf("") }

  Scaffold(
      modifier = Modifier.testTag(AlarmSetUpScreenTags.ROOT),
      topBar = {
        TopBar(
            leftText = stringResource(R.string.back),
            onLeftClick = onNavigateBack,
            middleText = stringResource(R.string.alarm_setup_title),
            rightText = stringResource(R.string.save),
            onRightClick = onSaveAlarm,
            leftTestTag = AlarmSetUpScreenTags.LEFT_TOPBAR_BUTTON,
            middleTestTag = AlarmSetUpScreenTags.MIDDLE_TOPBAR_TEXT,
            rightTestTag = AlarmSetUpScreenTags.RIGHT_TOPBAR_BUTTON)
      }) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
              // Time picker
              TimePicker(
                  modifier = Modifier.testTag(AlarmSetUpScreenTags.TIME_PICKER),
                  initialHour = hour,
                  initialMinute = minute,
                  hourSelectorTestTag = AlarmSetUpScreenTags.HOUR_PICKER,
                  minuteSelectorTestTag = AlarmSetUpScreenTags.MINUTE_PICKER,
                  onTimeChanged = { h, m ->
                    hour = h
                    minute = m
                  })

              // Alarm name input
              OutlinedTextField(
                  value = alarmName,
                  onValueChange = { alarmName = it },
                  placeholder = { Text(text = stringResource(R.string.alarm_name_placeholder)) },
                  modifier =
                      Modifier.fillMaxWidth()
                          .padding(paddingMedium)
                          .testTag(AlarmSetUpScreenTags.ALARM_NAME_FIELD))
            }
      }
}

@Preview
@Composable
fun AlarmSetUpScreenPreview() {
  AlarmSetUpScreen()
}
