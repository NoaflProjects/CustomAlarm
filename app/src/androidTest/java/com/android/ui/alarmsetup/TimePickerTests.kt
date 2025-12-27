package com.android.ui.alarmsetup

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.customalarm.ui.alarmsetup.TimePicker
import com.android.customalarm.ui.alarmsetup.TimePickerTestTags
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TimePickerTests {

  @get:Rule val composeTestRule = createComposeRule()

  // Initial time for testing
  private val startHour = 10
  private val startMinute = 30

  // Flags to verify that onTimeChanged is called
  private var hourChanged: Int? = null
  private var minuteChanged: Int? = null

  @Before
  fun setup() {
    // Create a TimePicker composable with default time
    composeTestRule.setContent {
      TimePicker(
          modifier = Modifier.fillMaxWidth(),
          selectedHour = startHour,
          selectedMinute = startMinute,
          onTimeChanged = { h, m ->
            hourChanged = h
            minuteChanged = m
          },
          hourSelectorTestTag = TimePickerTestTags.HOUR_PICKER,
          minuteSelectorTestTag = TimePickerTestTags.MINUTE_PICKER)
    }
  }

  @Test
  fun allComponentsDisplayed() {
    // Check that the hour picker is displayed
    composeTestRule.onNodeWithTag(TimePickerTestTags.HOUR_PICKER).assertIsDisplayed()

    // Check that the minute picker is displayed
    composeTestRule.onNodeWithTag(TimePickerTestTags.MINUTE_PICKER).assertIsDisplayed()
  }

  @Test
  fun timePicker_initialValuesDisplayed() {
    // Check that the initial hour is displayed
    composeTestRule.onNodeWithText(startHour.toString().padStart(2, '0')).assertIsDisplayed()

    // Check that the initial minute is displayed
    composeTestRule.onNodeWithText(startMinute.toString().padStart(2, '0')).assertIsDisplayed()
  }

  // Note: We cannot reliably test scrolling in LazyColumn with SnapFlingBehavior in unit tests.
  // The methods performScrollToIndex or swipe gestures do not work with snap behavior in Compose
  // tests. The scrolling behavior should be tested manually.
}
