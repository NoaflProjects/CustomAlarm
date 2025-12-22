package com.android.customalarm.ui.alarms

import com.android.customalarm.MainDispatcherRule
import com.android.customalarm.model.alarms.Alarm
import com.android.customalarm.model.alarms.AlarmsRepositoryInMemory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlarmsViewModelTests {

  @get:Rule val mainDispatcherRule = MainDispatcherRule()

  private lateinit var repository: AlarmsRepositoryInMemory
  private lateinit var viewModel: AlarmsViewModel

  @Before
  fun setup() {
    repository = AlarmsRepositoryInMemory()
    viewModel = AlarmsViewModel(repository)
  }

  @Test
  fun addAlarm_addsAlarmToState() = runTest {
    val alarm = Alarm(id = "1", time = "07:00")

    viewModel.addAlarm(alarm)

    val alarms = viewModel.alarms.first()
    Assert.assertEquals(1, alarms.size)
    Assert.assertEquals(alarm, alarms.first())
  }

  @Test
  fun toggleAlarm_updatesEnabledState() = runTest {
    val alarm = Alarm(id = "1", time = "07:00", isEnabled = false)
    repository.addAlarm(alarm)

    viewModel.toggleAlarm("1", true)

    val updatedAlarm = viewModel.alarms.first().first()
    Assert.assertTrue(updatedAlarm.isEnabled)
  }
}
