package com.android.customalarm.ui.alarmsetup

import com.android.customalarm.MainDispatcherRule
import com.android.customalarm.model.alarms.Alarm
import com.android.customalarm.model.alarms.AlarmsRepositoryInMemory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlarmSetUpVMTests {

  @get:Rule val mainDispatcherRule = MainDispatcherRule()

  private lateinit var repository: AlarmsRepositoryInMemory
  private lateinit var viewModel: AlarmSetUpViewModel

  @Before
  fun setup() {
    repository = AlarmsRepositoryInMemory()
    viewModel = AlarmSetUpViewModel(alarmsRepository = repository)
  }

  @Test
  fun `initial state is current time`() = runTest {
    // Create a new alarm to initialize the state
    viewModel.createNewAlarm()

    val state = viewModel.uiState.first()

    val now = java.util.Calendar.getInstance()
    val currentHour = now.get(java.util.Calendar.HOUR_OF_DAY)
    val currentMinute = now.get(java.util.Calendar.MINUTE)

    assertEquals(currentHour, state.selectedHour)
    assertEquals(currentMinute, state.selectedMinute)
    assertEquals(AlarmSetUpDefaults.DEFAULT_ALARM_NAME, state.alarmName)
  }

  @Test
  fun `onTimeChanged updates hour and minute`() = runTest {
    viewModel.onTimeChanged(8, 45)

    val state = viewModel.uiState.first()
    assertEquals(8, state.selectedHour)
    assertEquals(45, state.selectedMinute)
  }

  @Test
  fun `onAlarmNameChanged updates alarm name`() = runTest {
    viewModel.onAlarmNameChanged("Morning alarm")

    val state = viewModel.uiState.first()
    assertEquals("Morning alarm", state.alarmName)
  }

  @Test
  fun `saveAlarm adds new alarm when none exists`() = runTest {
    viewModel.onTimeChanged(7, 30)
    viewModel.onAlarmNameChanged("Work")

    viewModel.saveAlarm()
    advanceUntilIdle()

    val alarms = repository.getAlarms().first()
    assertEquals(1, alarms.size)

    val alarm = alarms.first()
    assertEquals("Work", alarm.name)
    assertEquals("07:30", alarm.time)
  }

  @Test
  fun `saveAlarm modifies existing alarm`() = runTest {
    val existingAlarm = Alarm(name = "Old alarm", time = "06:00")
    repository.addAlarm(existingAlarm)

    val alarmId = repository.getAlarms().first().first().id

    // Set the alarm ID in the ViewModel to edit the existing alarm
    viewModel.setAlarmId(alarmId)

    viewModel.onAlarmNameChanged("Updated alarm")
    viewModel.onTimeChanged(9, 15)

    viewModel.saveAlarm()
    advanceUntilIdle()

    // Check that the alarm was updated
    val updated = repository.getAlarmById(alarmId).first()
    assertNotNull(updated)
    assertEquals("Updated alarm", updated!!.name)
    assertEquals("09:15", updated.time)
  }

  @Test
  fun `setAlarmId loads existing alarm details`() = runTest {
    val existingAlarm = Alarm(name = "Existing alarm", time = "05:45")
    repository.addAlarm(existingAlarm)

    val alarmId = repository.getAlarms().first().first().id

    viewModel.setAlarmId(alarmId)
    advanceUntilIdle()

    val state = viewModel.uiState.first()
    assertEquals(5, state.selectedHour)
    assertEquals(45, state.selectedMinute)
    assertEquals("Existing alarm", state.alarmName)
  }
}
