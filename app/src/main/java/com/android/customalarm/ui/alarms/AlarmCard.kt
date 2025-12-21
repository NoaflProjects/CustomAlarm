package com.android.customalarm.ui.alarms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.android.customalarm.ui.theme.Dimens.fontSizeExtraLarge
import com.android.customalarm.ui.theme.Dimens.paddingMedium
import com.android.customalarm.ui.theme.Dimens.paddingSmall

/**
 * Composable representing a single alarm card with time display and enable/disable switch.
 *
 * @param time The time of the alarm to display.
 * @param isEnabled Initial enabled state of the alarm.
 * @param onToggle Callback invoked when the switch is toggled, passing the new state.
 * @param onClick Callback invoked when the card is clicked.
 */
@Composable
fun AlarmCard(
    time: String,
    isEnabled: Boolean = false,
    onToggle: (Boolean) -> Unit = {},
    onClick: () -> Unit = {}
) {
  var checked by remember { mutableStateOf(isEnabled) }

  Card(modifier = Modifier.fillMaxWidth().padding(all = paddingSmall).clickable { onClick() }) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(all = paddingMedium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
          // Alarm time display
          Text(text = time, fontSize = fontSizeExtraLarge)

          // Enable/disable switch
          Switch(
              checked = checked,
              onCheckedChange = {
                checked = it
                onToggle(it)
              })
        }
  }
}
