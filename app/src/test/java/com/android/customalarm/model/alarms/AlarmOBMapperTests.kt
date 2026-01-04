package com.android.customalarm.model.alarms

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class AlarmOBMapperTests {

  @Test
  fun toAlarm_mapsAllFieldsCorrectly() {
    val entity =
        AlarmEntity(
            objectBoxId = 42L,
            alarmId = "alarm-123",
            name = "Morning Alarm",
            time = "07:00",
            isEnabled = true)

    val alarm = entity.toAlarm()

    assertEquals("alarm-123", alarm.id)
    assertEquals("Morning Alarm", alarm.name)
    assertEquals("07:00", alarm.time)
    assertEquals(true, alarm.isEnabled)
  }

  @Test
  fun toEntity_mapsAllFieldsCorrectly() {
    val alarm = Alarm(id = "alarm-456", name = "Evening Alarm", time = "22:30", isEnabled = false)

    val entity = alarm.toEntity()

    assertEquals("alarm-456", entity.alarmId)
    assertEquals("Evening Alarm", entity.name)
    assertEquals("22:30", entity.time)
    assertFalse(entity.isEnabled)
  }

  @Test
  fun toAlarm_doesNotLeakObjectBoxId() {
    val entity =
        AlarmEntity(
            objectBoxId = 99L,
            alarmId = "alarm-id",
            name = "Test",
            time = "12:00",
            isEnabled = true)

    val alarm = entity.toAlarm()

    // The Alarm class does not have an objectBoxId field, so we just check the id field
    assertEquals("alarm-id", alarm.id)
  }

  @Test
  fun roundTrip_alarm_toEntity_toAlarm_preservesData() {
    val original =
        Alarm(id = "round-trip", name = "Round Trip Alarm", time = "06:45", isEnabled = true)

    val result = original.toEntity().toAlarm()

    assertEquals(original, result)
  }
}
