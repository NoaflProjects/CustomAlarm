package com.android.ui.alarmsetup

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.customalarm.ui.alarmsetup.TimePicker
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

object TimePickerTestTags {
  const val HOUR_PICKER = "HOUR_PICKER"
  const val MINUTE_PICKER = "MINUTE_PICKER"
}

@RunWith(AndroidJUnit4::class)
class TimePickerTests {

  @get:Rule val composeTestRule = createComposeRule()

  private var hour: Int = 0
  private val initialHour: Int = 10
  private var minute: Int = 0
  private val initialMinute: Int = 30

  @Before
  fun setup() {
    composeTestRule.setContent {
      TimePicker(
          initialHour = initialHour,
          initialMinute = initialMinute,
          hourSelectorTestTag = TimePickerTestTags.HOUR_PICKER,
          minuteSelectorTestTag = TimePickerTestTags.MINUTE_PICKER,
          onTimeChanged = { h, m ->
            hour = h
            minute = m
          },
          snapToCenter = false)
    }
  }

  @Test
  fun timePicker_allComponentsDisplayed() {
    // Verify hour picker is displayed
    composeTestRule.onNodeWithTag(TimePickerTestTags.HOUR_PICKER).assertIsDisplayed()

    // Verify minute picker is displayed
    composeTestRule.onNodeWithTag(TimePickerTestTags.MINUTE_PICKER).assertIsDisplayed()
  }

  @Test
  fun timePicker_initialValuesDisplayed() {
    // Verify initial hour is displayed
    composeTestRule.onNodeWithText(initialHour.toString().padStart(2, '0')).assertIsDisplayed()

    // Verify initial minute is displayed
    composeTestRule.onNodeWithText(initialMinute.toString().padStart(2, '0')).assertIsDisplayed()
  }

  @Test
  fun timePicker_onTimeChangedCalledOnInitialization() {
    // Verify onTimeChanged is called with initial values
    assert(hour == initialHour)
    assert(minute == initialMinute)
  }
}
