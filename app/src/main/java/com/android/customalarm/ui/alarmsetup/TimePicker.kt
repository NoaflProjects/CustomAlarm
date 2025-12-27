@file:OptIn(ExperimentalFoundationApi::class) // For rememberSnapFlingBehavior

package com.android.customalarm.ui.alarmsetup

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.customalarm.ui.theme.Dimens.circularPickerWidth
import com.android.customalarm.ui.theme.Dimens.fontSizeExtraLarge
import com.android.customalarm.ui.theme.Dimens.fontSizeMedium
import com.android.customalarm.ui.theme.Dimens.fontSizeSmall
import com.android.customalarm.ui.theme.Dimens.spacingMedium
import com.android.customalarm.ui.theme.Dimens.spacingVerySmall
import com.android.customalarm.ui.theme.Dimens.timePickerSelectionLineHeight

/** Constants for the TimePicker component */
private const val ITEM_HEIGHT = 50
private const val HOURS = 24
private const val MINUTES = 60
private const val REPEAT_FACTOR = 20

/** Test tags for TimePicker components */
object TimePickerTestTags {
  const val HOUR_PICKER = "HOUR_PICKER"
  const val MINUTE_PICKER = "MINUTE_PICKER"
}

/**
 * A composable TimePicker that allows users to select hours and minutes.
 *
 * @param modifier Modifier to be applied to the TimePicker.
 * @param selectedHour The currently selected hour (0-23).
 * @param selectedMinute The currently selected minute (0-59).
 * @param onTimeChanged Callback invoked when the selected time changes.
 * @param hourSelectorTestTag Test tag for the hour selector (for testing purposes).
 * @param minuteSelectorTestTag Test tag for the minute selector (for testing purposes).
 */
@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    selectedHour: Int,
    selectedMinute: Int,
    onTimeChanged: (hour: Int, minute: Int) -> Unit,
    hourSelectorTestTag: String = "",
    minuteSelectorTestTag: String = ""
) {
  val visibleItems = 3 // Number of items visible at once

  // States for the hour and minute pickers
  val hourState =
      rememberLazyListState(initialFirstVisibleItemIndex = HOURS * REPEAT_FACTOR / 2 + selectedHour)
  val minuteState =
      rememberLazyListState(
          initialFirstVisibleItemIndex = MINUTES * REPEAT_FACTOR / 2 + selectedMinute)

  // Snap inertia for smooth scrolling
  val hourSnap = rememberSnapFlingBehavior(lazyListState = hourState)
  val minuteSnap = rememberSnapFlingBehavior(lazyListState = minuteState)

  // Derived states to find the centered hour and minute
  val centeredHour by remember {
    derivedStateOf {
      val layoutInfo = hourState.layoutInfo
      val viewportCenter = layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset / 2

      // Find the item closest to the center of the viewport
      val index =
          layoutInfo.visibleItemsInfo
              .minByOrNull { item ->
                val itemCenter = item.offset + item.size / 2
                kotlin.math.abs(n = itemCenter - viewportCenter)
              }
              ?.index ?: selectedHour

      index % HOURS
    }
  }
  val centeredMinute by remember {
    derivedStateOf {
      val layoutInfo = minuteState.layoutInfo
      val viewportCenter = layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset / 2

      // Find the item closest to the center of the viewport
      val index =
          layoutInfo.visibleItemsInfo
              .minByOrNull { item ->
                val itemCenter = item.offset + item.size / 2
                kotlin.math.abs(n = itemCenter - viewportCenter)
              }
              ?.index ?: selectedMinute

      index % MINUTES
    }
  }

  // Call onTimeChanged whenever the centered hour or minute changes
  LaunchedEffect(key1 = centeredHour, key2 = centeredMinute) {
    onTimeChanged(centeredHour, centeredMinute)
  }

  // Layout for the TimePicker
  Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {

          // Hours selector
          Box(
              modifier =
                  Modifier.width(circularPickerWidth)
                      .height(height = (ITEM_HEIGHT * visibleItems).dp)
                      .testTag(hourSelectorTestTag)) {
                LazyColumn(
                    state = hourState,
                    flingBehavior = hourSnap,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                      items(count = HOURS * REPEAT_FACTOR) { index ->
                        val hour = index % HOURS
                        val isSelected = hour == centeredHour
                        Text(
                            text = hour.toString().padStart(length = 2, padChar = '0'),
                            fontSize = if (isSelected) fontSizeExtraLarge else fontSizeMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().height(height = ITEM_HEIGHT.dp))
                      }
                    }

                // Selection line
                Box(
                    modifier =
                        Modifier.align(Alignment.Center)
                            .fillMaxWidth()
                            .height(height = timePickerSelectionLineHeight))
              }

          Spacer(Modifier.width(width = spacingMedium))

          // Minutes selector
          Box(
              modifier =
                  Modifier.width(width = circularPickerWidth)
                      .height(height = (ITEM_HEIGHT * visibleItems).dp)
                      .testTag(minuteSelectorTestTag)) {
                LazyColumn(
                    state = minuteState,
                    flingBehavior = minuteSnap,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                      items(count = MINUTES * REPEAT_FACTOR) { index ->
                        val minute = index % MINUTES
                        val isSelected = minute == centeredMinute
                        Text(
                            text = minute.toString().padStart(length = 2, padChar = '0'),
                            fontSize = if (isSelected) fontSizeExtraLarge else fontSizeMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().height(height = ITEM_HEIGHT.dp))
                      }
                    }

                Box(
                    modifier =
                        Modifier.align(Alignment.Center)
                            .fillMaxWidth()
                            .height(height = timePickerSelectionLineHeight))
              }
        }

    Spacer(Modifier.height(height = spacingVerySmall))

    // Display the selected time
    Text(
        text = "The alarm is set for %02d:%02d".format(centeredHour, centeredMinute),
        fontSize = fontSizeSmall,
        fontWeight = FontWeight.Normal)
  }
}
