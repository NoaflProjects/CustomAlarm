package com.android.ui.alarms

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.android.customalarm.R
import com.android.customalarm.model.alarms.Alarm
import com.android.customalarm.model.alarms.AlarmsRepository
import com.android.customalarm.model.alarms.AlarmsRepositoryInMemory
import com.android.customalarm.ui.alarms.AlarmsScreen
import com.android.customalarm.ui.alarms.AlarmsScreenTestTags
import com.android.customalarm.ui.alarms.AlarmsViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlarmsScreenTests {

  @get:Rule val composeRule = createComposeRule()

  lateinit var alarmsRepository: AlarmsRepository
  lateinit var alarmsViewModel: AlarmsViewModel

  // Sample alarms for testing
  val alarms: List<Alarm> =
      listOf(
          Alarm(id = "1", time = "07:00", isEnabled = true),
          Alarm(id = "2", time = "08:30", isEnabled = false))

  @Before
  fun setup() {
    alarmsRepository = AlarmsRepositoryInMemory()
    alarmsViewModel = AlarmsViewModel(alarmsRepository = alarmsRepository)

    composeRule.setContent { AlarmsScreen(alarmsViewModel = alarmsViewModel) }
  }

  fun addSampleAlarms() {
    alarms.forEach { alarm -> composeRule.runOnUiThread { alarmsViewModel.addAlarm(alarm) } }
  }

  @Test
  fun allElements_displayed() {
    // Check root is displayed
    composeRule.onNodeWithTag(AlarmsScreenTestTags.ROOT).assertIsDisplayed()

    // Check title is displayed
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val expectedTitle = context.getString(R.string.alarms_title)
    composeRule.onNodeWithText(expectedTitle).assertIsDisplayed()

    // Check add alarm button is displayed
    composeRule.onNodeWithTag(AlarmsScreenTestTags.ADD_ALARM_BUTTON).assertIsDisplayed()
  }

  @Test
  fun noAlarms_displayNoAlarmsMessage() {
    // Check no alarms message is displayed
    composeRule.onNodeWithTag(AlarmsScreenTestTags.NO_ALARMS_MESSAGE).assertIsDisplayed()
  }

  @Test
  fun withAlarms_displayAlarmCards() {
    // Add sample alarms to the repository
    addSampleAlarms()

    // Verify each alarm card is displayed
    alarms.forEach { alarm ->
      composeRule.onNodeWithTag(AlarmsScreenTestTags.alarmCard(alarm.id)).assertIsDisplayed()
    }
  }

  @Test
  fun withAlarms_checkToggleAlarm() {
    // Add sample alarms to the repository
    addSampleAlarms()

    // Pick an alarm to toggle
    val alarm = alarms[0]

    // Toggle the alarm on
    composeRule.runOnUiThread { alarmsViewModel.toggleAlarm(alarm.id, true) }

    // Verify the alarm card is displayed
    composeRule.onNodeWithTag(AlarmsScreenTestTags.alarmCard(alarm.id)).assertIsDisplayed()

    // Check the switch is in the correct state
    composeRule.onNodeWithTag(AlarmsScreenTestTags.alarmCardSwitch(alarm.id)).assertIsOn()
  }
}
