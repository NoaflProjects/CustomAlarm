package com.android.ui.alarmsetup

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.customalarm.model.alarms.Alarm
import com.android.customalarm.model.alarms.AlarmsRepository
import com.android.customalarm.model.alarms.AlarmsRepositoryInMemory
import com.android.customalarm.ui.alarmsetup.AlarmSetUpScreen
import com.android.customalarm.ui.alarmsetup.AlarmSetUpScreenTags
import com.android.customalarm.ui.alarmsetup.AlarmSetUpViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlarmSetUpScreenTests {

  @get:Rule val composeRule = createComposeRule()

  private lateinit var alarmsRepository: AlarmsRepository
  private lateinit var alarmSetUpVM: AlarmSetUpViewModel

  private var onNavigateBackCalled = false
  private var onSaveAlarmCalled = false

  @Before
  fun setup() {
    alarmsRepository = AlarmsRepositoryInMemory()
    alarmSetUpVM = AlarmSetUpViewModel(alarmsRepository = alarmsRepository)

    composeRule.setContent {
      AlarmSetUpScreen(
          alarmSetUpViewModel = alarmSetUpVM,
          onNavigateBack = { onNavigateBackCalled = true },
          onSaveAlarm = { onSaveAlarmCalled = true })
    }
  }

  @Test
  fun alarmSetupScreen_isDisplayed() {
    composeRule.onNodeWithTag(AlarmSetUpScreenTags.ROOT).assertIsDisplayed()

    composeRule.onNodeWithTag(AlarmSetUpScreenTags.LEFT_TOPBAR_BUTTON).assertIsDisplayed()

    composeRule.onNodeWithTag(AlarmSetUpScreenTags.MIDDLE_TOPBAR_TEXT).assertIsDisplayed()

    composeRule.onNodeWithTag(AlarmSetUpScreenTags.RIGHT_TOPBAR_BUTTON).assertIsDisplayed()

    composeRule.onNodeWithTag(AlarmSetUpScreenTags.TIME_PICKER).assertIsDisplayed()

    composeRule.onNodeWithTag(AlarmSetUpScreenTags.ALARM_NAME_FIELD).assertIsDisplayed()
  }

  @Test
  fun alarmNameField_isEditable_andUpdatesViewModel() {
    val input = "Morning Alarm"

    composeRule.onNodeWithTag(AlarmSetUpScreenTags.ALARM_NAME_FIELD).performTextInput(input)

    composeRule.runOnIdle { assertEquals(input, alarmSetUpVM.uiState.value.alarmName) }
  }

  @Test
  fun clickingSaveButton_savesAlarmInRepository() {
    composeRule.onNodeWithTag(AlarmSetUpScreenTags.ALARM_NAME_FIELD).performTextInput("Work")

    composeRule.onNodeWithTag(AlarmSetUpScreenTags.RIGHT_TOPBAR_BUTTON).performClick()

    composeRule.runOnIdle {
      val alarms = (alarmsRepository as AlarmsRepositoryInMemory).getAlarms()
      // on consomme le Flow
      val list = kotlinx.coroutines.runBlocking { alarms.first() }

      assertEquals(1, list.size)
      assertEquals("Work", list.first().name)
    }
  }

  @Test
  fun clickingBackButton_triggersOnNavigateBack() {
    composeRule.onNodeWithTag(AlarmSetUpScreenTags.LEFT_TOPBAR_BUTTON).performClick()

    assertTrue(onNavigateBackCalled)
  }

  @Test
  fun clickingSaveButton_triggersOnSaveAlarmCallback() {
    composeRule.onNodeWithTag(AlarmSetUpScreenTags.RIGHT_TOPBAR_BUTTON).performClick()

    assertTrue(onSaveAlarmCalled)
  }
}

@RunWith(AndroidJUnit4::class)
class AlarmSetUpScreenWithExistingAlarmTests {

  @get:Rule val composeRule = createComposeRule()

  private lateinit var alarmsRepository: AlarmsRepository

  @Test
  fun alarmSetupScreen_displaysExistingAlarmNameAndTime() = runTest {
    // Add an existing alarm to the repository
    alarmsRepository = AlarmsRepositoryInMemory()
    val existingAlarm = Alarm(id = "alarm1", name = "Evening Alarm", time = "21:45")
    alarmsRepository.addAlarm(existingAlarm)

    val viewModel = AlarmSetUpViewModel(alarmsRepository)

    composeRule.setContent { AlarmSetUpScreen(alarmSetUpViewModel = viewModel, alarmId = "alarm1") }

    composeRule.waitForIdle()

    // Check that the name is displayed correctly
    composeRule
        .onNodeWithTag(AlarmSetUpScreenTags.ALARM_NAME_FIELD)
        .assertIsDisplayed()
        .assertTextEquals("Evening Alarm")

    // Check that the time is displayed correctly
    composeRule.onNodeWithText("21").assertIsDisplayed()
    composeRule.onNodeWithText("45").assertIsDisplayed()
  }
}
