package com.android.customalarm.model.alarms

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlarmsRepositoryInMemoryTests {

  private lateinit var repository: AlarmsRepositoryInMemory
  private lateinit var alarm1: Alarm
  private lateinit var alarm2: Alarm

  @Before
  fun setup() = runBlocking {
    repository = AlarmsRepositoryInMemory()

    // Alarmes communes à plusieurs tests
    alarm1 = Alarm(id = "1", time = "07:00")
    alarm2 = Alarm(id = "2", time = "08:00")

    // Ajout initial d'alarmes
    repository.addAlarm(alarm1)
    repository.addAlarm(alarm2)
  }

  @Test
  fun addAlarm_addsANewAlarm() = runBlocking {
    val alarm3 = Alarm(id = "3", time = "09:00")
    repository.addAlarm(alarm3)

    val alarms = repository.getAlarms().first()
    assertEquals(3, alarms.size)
    assertTrue(alarms.contains(alarm3))
  }

  @Test
  fun removeAlarm_removesAnExistingAlarm() = runBlocking {
    repository.removeAlarm("1")
    val alarms = repository.getAlarms().first()
    assertEquals(1, alarms.size)
    assertFalse(alarms.any { it.id == "1" })
  }

  @Test
  fun removeAlarm_throwsExceptionIfAlarmDoesNotExist() = runBlocking {
    try {
      repository.removeAlarm("non-existent-id")
      fail("Expected IllegalArgumentException to be thrown")
    } catch (e: IllegalArgumentException) {
      assertTrue(e.message!!.contains("does not exist"))
    }
  }

  @Test
  fun modifyAlarm_updatesAnExistingAlarm() = runBlocking {
    val updatedAlarm = Alarm(id = "1", time = "07:30")
    repository.modifyAlarm("1", updatedAlarm)

    val alarms = repository.getAlarms().first()
    val alarm = alarms.first { it.id == "1" }
    assertEquals("07:30", alarm.time)
  }

  @Test
  fun toggleAlarm_changesTheEnabledState() = runBlocking {
    repository.toggleAlarm("2", true)
    val alarm = repository.getAlarms().first().first { it.id == "2" }
    assertTrue(alarm.isEnabled)
  }

  @Test
  fun requireAlarmExists_throwsIfAlarmMissing() = runBlocking {
    try {
      repository.requireAlarmExists("missing-id")
      fail("Expected IllegalArgumentException to be thrown")
    } catch (e: IllegalArgumentException) {
      assertTrue(e.message!!.contains("does not exist"))
    }
  }

  @Test
  fun getAlarmById_returnsCorrectAlarm() = runBlocking {
    val alarmFlow = repository.getAlarmById("1")
    val alarm = alarmFlow.first()
    assertNotNull(alarm)
    assertEquals("1", alarm?.id)
    assertEquals("07:00", alarm?.time)
  }

  @Test
  fun getAlarmById_returnsNullForNonExistentAlarm() = runBlocking {
    val alarmFlow = repository.getAlarmById("non-existent-id")
    val alarm = alarmFlow.first()
    assertNull(alarm)
  }

  @Test
  fun getAlarms_returnsAllAlarms() = runBlocking {
    val alarms = repository.getAlarms().first()
    assertEquals(2, alarms.size)
    assertTrue(alarms.contains(alarm1))
    assertTrue(alarms.contains(alarm2))
  }
}
