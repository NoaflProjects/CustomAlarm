package com.android.customalarm.ui.alarmsetup

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.android.customalarm.ui.theme.Dimens.CircularPickerWidth
import com.android.customalarm.ui.theme.Dimens.fontSizeExtraLarge
import com.android.customalarm.ui.theme.Dimens.fontSizeMedium
import com.android.customalarm.ui.theme.Dimens.spacingMedium
import com.android.customalarm.ui.theme.Dimens.timePickerItemHeight
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

/**
 * A composable TimePicker that allows users to select hour and minute using circular pickers.
 *
 * @param modifier Modifier to be applied to the TimePicker.
 * @param initialHour Initial hour to be selected (0-23).
 * @param initialMinute Initial minute to be selected (0-59).
 * @param onTimeChanged Lambda function called when the time is changed, providing the selected hour
 *   and minute.
 */
@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    initialHour: Int = 12,
    initialMinute: Int = 0,
    onTimeChanged: (hour: Int, minute: Int) -> Unit = { _, _ -> }
) {
  // State for selected hour and minute
  var selectedHour by remember { mutableIntStateOf(initialHour) }
  var selectedMinute by remember { mutableIntStateOf(initialMinute) }

  // LazyListStates for hour and minute pickers
  val hourState = rememberLazyListState(initialFirstVisibleItemIndex = initialHour + 1000 * 24 / 2)
  val minuteState =
      rememberLazyListState(initialFirstVisibleItemIndex = initialMinute + 1000 * 60 / 2)

  Row(
      modifier = modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically) {

        // Hour picker
        CircularPicker(
            range = 0..23,
            state = hourState,
            onSelectedChange = {
              selectedHour = it
              onTimeChanged(selectedHour, selectedMinute)
            })

        Spacer(modifier = Modifier.width(spacingMedium))

        // Minute picker
        CircularPicker(
            range = 0..59,
            state = minuteState,
            onSelectedChange = {
              selectedMinute = it
              onTimeChanged(selectedHour, selectedMinute)
            })
      }
}

/**
 * A composable CircularPicker that displays a scrollable list of numbers in a circular fashion.
 *
 * @param range The range of integers to display in the picker.
 * @param state The LazyListState to control the scroll position.
 * @param modifier Modifier to be applied to the CircularPicker.
 * @param onSelectedChange Lambda function called when the selected value changes.
 */
@SuppressLint("FrequentlyChangingValue")
@Composable
fun CircularPicker(
    range: IntRange,
    state: androidx.compose.foundation.lazy.LazyListState,
    modifier: Modifier = Modifier,
    onSelectedChange: (Int) -> Unit
) {
  // Number of visible items
  val visibleCount = 3

  // To create a circular effect, repeat the range multiple times
  val repeatCount = 1000
  val totalCount = range.count() * repeatCount

  val coroutineScope = rememberCoroutineScope()

  val itemHeightPx = with(LocalDensity.current) { timePickerItemHeight.toPx() }

  Box(modifier = modifier.width(CircularPickerWidth).height(timePickerItemHeight * visibleCount)) {
    LazyColumn(
        state = state,
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding =
            PaddingValues(vertical = timePickerItemHeight * ((visibleCount - 1) / 2))) {
          items(totalCount) { index ->

            // Calculate the actual value considering the circular nature
            val value = range.first + (index % range.count())
            val offsetIndex = state.firstVisibleItemScrollOffset / itemHeightPx
            val centerIndex = state.firstVisibleItemIndex + offsetIndex.roundToInt()
            val centerValue = range.first + (centerIndex % range.count())
            val isSelected = value == centerValue

            Text(
                // Pad with leading zero
                text = value.toString().padStart(2, '0'),
                fontSize = if (isSelected) fontSizeExtraLarge else fontSizeMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color =
                    if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier =
                    Modifier.height(timePickerItemHeight)
                        .fillMaxWidth()
                        .wrapContentHeight(Alignment.CenterVertically)
                        .clickable {
                          coroutineScope.launch {
                            state.animateScrollToItem(index)
                            onSelectedChange(value)
                          }
                        })
          }
        }
  }

  // Listen to scroll state changes to detect when scrolling stops
  LaunchedEffect(state.isScrollInProgress) {
    snapshotFlow { state.isScrollInProgress }
        .collect { scrolling ->
          // When scrolling stops, snap to the nearest item
          if (!scrolling) {
            val offsetIndex = state.firstVisibleItemScrollOffset / itemHeightPx
            val centerIndex = state.firstVisibleItemIndex + offsetIndex.roundToInt()
            coroutineScope.launch {
              state.animateScrollToItem(centerIndex)
              onSelectedChange(range.first + (centerIndex % range.count()))
            }
          }
        }
  }
}
