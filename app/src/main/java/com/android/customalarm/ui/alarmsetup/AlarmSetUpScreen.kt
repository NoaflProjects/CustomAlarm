package com.android.customalarm.ui.alarmsetup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.customalarm.R
import com.android.customalarm.ui.common.TopBar
import com.android.customalarm.ui.theme.Dimens.fontSizeSmall
import com.android.customalarm.ui.theme.Dimens.paddingMedium
import com.android.customalarm.ui.theme.Dimens.spacingVerySmall

/** Test tags for AlarmSetUpScreen components */
object AlarmSetUpScreenTags {
  const val ROOT = "alarm_setup_screen_root"
  const val LEFT_TOPBAR_BUTTON = "alarm_setup_screen_left_topbar_button"
  const val MIDDLE_TOPBAR_TEXT = "alarm_setup_screen_middle_topbar_text"
  const val RIGHT_TOPBAR_BUTTON = "alarm_setup_screen_right_topbar_button"
  const val TIME_PICKER = "alarm_setup_screen_time_picker"
  const val HOUR_PICKER = "alarm_setup_screen_hour_picker"
  const val MINUTE_PICKER = "alarm_setup_screen_minute_picker"
  const val SELECTED_TIME_TEXT = "alarm_setup_screen_selected_time_text"
  const val ALARM_NAME_FIELD = "alarm_setup_screen_alarm_name_field"
}

/**
 * Composable function for the Alarm Setup Screen.
 *
 * @param alarmSetUpViewModel ViewModel managing the state and logic for the Alarm Setup screen.
 * @param alarmId Optional ID of the alarm being edited; null if creating a new alarm.
 * @param onNavigateBack Lambda function to be called when the back button is clicked.
 * @param onSaveAlarm Lambda function to be called when the save button is clicked.
 */
@Composable
fun AlarmSetUpScreen(
    alarmSetUpViewModel: AlarmSetUpViewModel = viewModel(),
    alarmId: String? = null,
    onNavigateBack: () -> Unit = {},
    onSaveAlarm: () -> Unit = {}
) {

  // State for the selected time
  val uiState = alarmSetUpViewModel.uiState.collectAsState()

  // State to track if the alarm data is loaded
  val isLoaded = alarmSetUpViewModel.isLoaded.collectAsState()

  // Load the alarm or initialize a new one
  LaunchedEffect(alarmId) {
    if (alarmId != null) {
      alarmSetUpViewModel.setAlarmId(alarmId)
    } else {
      alarmSetUpViewModel.createNewAlarm()
    }
  }

  // Display the UI only after the alarm data is loaded
  if (isLoaded.value) {
    Scaffold(
        modifier = Modifier.testTag(tag = AlarmSetUpScreenTags.ROOT),
        topBar = {
          TopBar(
              leftText = stringResource(id = R.string.back),
              onLeftClick = onNavigateBack,
              middleText = stringResource(id = R.string.alarm_setup_title),
              rightText = stringResource(id = R.string.save),
              onRightClick = {
                alarmSetUpViewModel.saveAlarm()
                onSaveAlarm()
              },
              leftTestTag = AlarmSetUpScreenTags.LEFT_TOPBAR_BUTTON,
              middleTestTag = AlarmSetUpScreenTags.MIDDLE_TOPBAR_TEXT,
              rightTestTag = AlarmSetUpScreenTags.RIGHT_TOPBAR_BUTTON)
        }) { innerPadding ->
          Column(
              modifier = Modifier.fillMaxSize().padding(paddingValues = innerPadding),
              verticalArrangement = Arrangement.Center,
              horizontalAlignment = Alignment.CenterHorizontally) {
                // Time picker
                TimePicker(
                    modifier = Modifier.testTag(tag = AlarmSetUpScreenTags.TIME_PICKER),
                    selectedHour = uiState.value.selectedHour,
                    selectedMinute = uiState.value.selectedMinute,
                    onTimeChanged = { hour, minute ->
                      alarmSetUpViewModel.onTimeChanged(hour, minute)
                    },
                    hourSelectorTestTag = AlarmSetUpScreenTags.HOUR_PICKER,
                    minuteSelectorTestTag = AlarmSetUpScreenTags.MINUTE_PICKER)

                Spacer(Modifier.height(height = spacingVerySmall))

                // Display the selected time
                Text(
                    text =
                        stringResource(
                            id = R.string.alarm_set_for_time,
                            uiState.value.selectedHour,
                            uiState.value.selectedMinute),
                    fontSize = fontSizeSmall,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.testTag(tag = AlarmSetUpScreenTags.SELECTED_TIME_TEXT))

                Spacer(Modifier.height(height = spacingVerySmall))

                // Alarm name input
                OutlinedTextField(
                    value = uiState.value.alarmName,
                    onValueChange = alarmSetUpViewModel::onAlarmNameChanged,
                    placeholder = {
                      Text(text = stringResource(id = R.string.alarm_name_placeholder))
                    },
                    modifier =
                        Modifier.fillMaxWidth()
                            .padding(all = paddingMedium)
                            .testTag(tag = AlarmSetUpScreenTags.ALARM_NAME_FIELD))

                // Delete button for existing alarms only
                if (alarmId != null) {
                  Spacer(Modifier.height(height = spacingVerySmall))

                  // Delete button
                  Button(
                      onClick = {
                        alarmSetUpViewModel.deleteAlarm()
                        onNavigateBack()
                      }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(id = R.string.delete))
                        Spacer(modifier = Modifier.width(width = spacingVerySmall))
                        Text(text = stringResource(id = R.string.delete))
                      }
                }
              }
        }
  }
}
