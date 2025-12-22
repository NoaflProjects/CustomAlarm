package com.android.customalarm.ui.alarms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.customalarm.R
import com.android.customalarm.ui.theme.Dimens.paddingMedium
import com.android.customalarm.ui.theme.Dimens.spacingSmall

/** Test tags for the AlarmsScreen composable */
object AlarmsScreenTestTags {
  const val ROOT = "alarmsScreenRoot"
  const val ADD_ALARM_BUTTON = "addAlarmButton"
  const val NO_ALARMS_MESSAGE = "noAlarmsMessage"

  fun alarmCard(alarmId: String) = "alarmCard_$alarmId"

  fun alarmCardSwitch(alarmId: String) = "alarmCard_${alarmId}_switch"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmsScreen(
    alarmsViewModel: AlarmsViewModel = viewModel(),
    onAddAlarm: () -> Unit = {},
    onClickAlarm: () -> Unit = {}
) {

  // Collect the alarms state from the ViewModel
  val alarms = alarmsViewModel.alarms.collectAsState()

  Scaffold(
      modifier = Modifier.testTag(AlarmsScreenTestTags.ROOT),
      topBar = { TopAppBar(title = { Text(stringResource(R.string.alarms_title)) }) },
      floatingActionButton = {
        FloatingActionButton(
            modifier = Modifier.testTag(AlarmsScreenTestTags.ADD_ALARM_BUTTON),
            onClick = { onAddAlarm() }) {
              Icon(
                  imageVector = Icons.Default.Add,
                  contentDescription = stringResource(R.string.add_alarm))
            }
      }) { innerPadding ->
        if (alarms.value.isEmpty()) {
          Text(
              modifier =
                  Modifier.testTag(AlarmsScreenTestTags.NO_ALARMS_MESSAGE)
                      .padding(paddingValues = innerPadding)
                      .fillMaxSize()
                      .wrapContentSize(),
              text = stringResource(R.string.no_alarms_message))
        } else {
          LazyColumn(
              modifier = Modifier.padding(paddingValues = innerPadding).fillMaxSize(),
              contentPadding = PaddingValues(all = paddingMedium),
              verticalArrangement = Arrangement.spacedBy(space = spacingSmall)) {
                items(alarms.value, key = { it.id }) { alarm ->
                  AlarmCard(
                      time = alarm.time,
                      isEnabled = alarm.isEnabled,
                      onToggle = { enabled -> alarmsViewModel.toggleAlarm(alarm.id, enabled) },
                      onClick = onClickAlarm,
                      cardTestTag = AlarmsScreenTestTags.alarmCard(alarm.id),
                      switchTestTag = AlarmsScreenTestTags.alarmCardSwitch(alarm.id))
                }
              }
        }
      }
}
