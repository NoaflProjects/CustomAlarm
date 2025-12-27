@file:OptIn(ExperimentalFoundationApi::class) // For rememberSnapFlingBehavior

package com.android.customalarm.ui.alarmsetup

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import com.android.customalarm.ui.theme.Dimens.spacingMedium
import com.android.customalarm.ui.theme.Dimens.timePickerSelectionLineHeight

/** Constants for the TimePicker component */
private const val ITEM_HEIGHT = 50
private const val HOURS = 24
private const val MINUTES = 60
private const val REPEAT_FACTOR = 20
private const val VISIBLE_ITEMS = 3 // Number of visible items in the picker

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
    hourSelectorTestTag: String = TimePickerTestTags.HOUR_PICKER,
    minuteSelectorTestTag: String = TimePickerTestTags.MINUTE_PICKER
) {
  // States for the hour and minute pickers
  val hourState =
      rememberLazyListState(
          initialFirstVisibleItemIndex =
              HOURS * REPEAT_FACTOR / 2 + selectedHour - VISIBLE_ITEMS / 2)
  val minuteState =
      rememberLazyListState(
          initialFirstVisibleItemIndex =
              MINUTES * REPEAT_FACTOR / 2 + selectedMinute - VISIBLE_ITEMS / 2)

  // Snap fling behaviors for smooth scrolling
  val hourSnap = rememberSnapFlingBehavior(lazyListState = hourState)
  val minuteSnap = rememberSnapFlingBehavior(lazyListState = minuteState)

  /** Utility function to get the item currently centered in the LazyColumn. */
  fun getCenteredItem(state: LazyListState, count: Int, default: Int): Int {
    val layoutInfo = state.layoutInfo
    val viewportCenter = layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset / 2
    val index =
        layoutInfo.visibleItemsInfo
            .minByOrNull { item ->
              val itemCenter = item.offset + item.size / 2
              kotlin.math.abs(itemCenter - viewportCenter)
            }
            ?.index ?: default
    return index % count
  }

  // Derived states to track the centered hour and minute
  val centeredHour by remember {
    derivedStateOf { getCenteredItem(hourState, HOURS, default = selectedHour) }
  }
  val centeredMinute by remember {
    derivedStateOf { getCenteredItem(minuteState, MINUTES, default = selectedMinute) }
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
          // Hour selector
          TimeSelector(
              state = hourState,
              snap = hourSnap,
              count = HOURS,
              centered = centeredHour,
              testTag = hourSelectorTestTag)
          Spacer(Modifier.width(width = spacingMedium))
          // Minute selector
          TimeSelector(
              state = minuteState,
              snap = minuteSnap,
              count = MINUTES,
              centered = centeredMinute,
              testTag = minuteSelectorTestTag)
        }
  }
}

/**
 * A composable representing a single time selector column (hours or minutes).
 *
 * @param state LazyListState for the column.
 * @param snap FlingBehavior used for snapping.
 * @param count Number of items (hours or minutes).
 * @param centered Currently selected item.
 * @param testTag Test tag for the selector.
 */
@Composable
private fun TimeSelector(
    state: LazyListState,
    snap: FlingBehavior,
    count: Int,
    centered: Int,
    testTag: String
) {
  Box(
      modifier =
          Modifier.width(circularPickerWidth)
              .height(height = (ITEM_HEIGHT * VISIBLE_ITEMS).dp)
              .testTag(testTag)) {
        LazyColumn(
            state = state,
            flingBehavior = snap,
            horizontalAlignment = Alignment.CenterHorizontally) {
              items(count = count * REPEAT_FACTOR) { index ->
                val value = index % count
                val isSelected = value == centered
                Text(
                    text = value.toString().padStart(length = 2, padChar = '0'),
                    fontSize = if (isSelected) fontSizeExtraLarge else fontSizeMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().height(ITEM_HEIGHT.dp))
              }
            }
        // Selection line overlay
        Box(
            modifier =
                Modifier.align(Alignment.Center)
                    .fillMaxWidth()
                    .height(timePickerSelectionLineHeight))
      }
}
