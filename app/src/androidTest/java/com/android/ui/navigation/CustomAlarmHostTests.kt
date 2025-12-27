package com.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.customalarm.ui.alarms.AlarmsScreenTestTags
import com.android.customalarm.ui.alarmsetup.AlarmSetUpScreenTags
import com.android.customalarm.ui.navigation.CustomAlarmNavHost
import com.android.customalarm.ui.navigation.Routes
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Tests for the CustomAlarmNavHost navigation graph. */
@RunWith(AndroidJUnit4::class)
class CustomAlarmHostTests {

  @get:Rule val composeRule = createComposeRule()

  @Test
  fun navHost_startsAtAlarmsScreen() {
    composeRule.setContent { TestNavHost() }

    composeRule.onNodeWithTag(AlarmsScreenTestTags.ROOT).assertExists().assertIsDisplayed()
  }

  @Test
  fun navigateToAlarmSetupScreenAndGoBack() {
    composeRule.setContent { TestNavHost() }

    composeRule.onNodeWithTag(AlarmsScreenTestTags.ADD_ALARM_BUTTON).assertExists().performClick()

    composeRule.onNodeWithTag(AlarmSetUpScreenTags.ROOT).assertExists().assertIsDisplayed()

    composeRule.onNodeWithTag(AlarmSetUpScreenTags.LEFT_TOPBAR_BUTTON).assertExists().performClick()

    composeRule.onNodeWithTag(AlarmsScreenTestTags.ROOT).assertExists().assertIsDisplayed()
  }

  @Test
  fun navigateToAlarmSetupWithIdScreenAndGoBack() {
    val testAlarmId = "test-id-123"

    // Simulate navigation directly to alarmSetup/{alarmId}
    composeRule.setContent { TestNavHost(startDestination = "${Routes.ALARM_SETUP}/$testAlarmId") }

    // Assert that the AlarmSetUpScreen is displayed with the correct test tag
    composeRule.onNodeWithTag(AlarmSetUpScreenTags.ROOT).assertExists().assertIsDisplayed()

    // Go back
    composeRule.onNodeWithTag(AlarmSetUpScreenTags.LEFT_TOPBAR_BUTTON).assertExists().performClick()
  }

  @Composable
  private fun TestNavHost(startDestination: String = "alarms") {
    rememberNavController()
    CustomAlarmNavHost(startDestination = startDestination)
  }
}
