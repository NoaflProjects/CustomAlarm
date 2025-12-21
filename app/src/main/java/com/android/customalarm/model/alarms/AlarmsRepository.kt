package com.android.customalarm.model.alarms

import kotlinx.coroutines.flow.Flow

/** Repository interface for managing alarms */
interface AlarmsRepository {

  /** Returns a flow of the list of alarms */
  fun getAlarms(): Flow<List<Alarm>>

  /**
   * Adds a new alarm
   *
   * @param alarm The alarm to be added
   */
  suspend fun addAlarm(alarm: Alarm)

  /**
   * Removes an existing alarm
   *
   * Implementations should call [requireAlarmExists] to ensure the alarm exists before removal.
   *
   * @param alarmId The ID of the alarm to be removed
   */
  suspend fun removeAlarm(alarmId: String)

  /**
   * Modifies an existing alarm
   *
   * Implementations should call [requireAlarmExists] to ensure the alarm exists before
   * modification.
   *
   * @param alarmId The ID of the alarm to be modified
   * @param alarm The updated alarm data
   */
  suspend fun modifyAlarm(alarmId: String, alarm: Alarm)

  /**
   * Toggles the enabled state of an alarm
   *
   * Implementations should call [requireAlarmExists] to ensure the alarm exists before toggling.
   *
   * @param alarmId The ID of the alarm to be toggled
   * @param enabled The new enabled state
   */
  suspend fun toggleAlarm(alarmId: String, enabled: Boolean)

  /**
   * Ensures that an alarm with the given ID exists.
   *
   * @param alarmId The ID of the alarm to check
   * @throws IllegalArgumentException if the alarm does not exist
   */
  suspend fun requireAlarmExists(alarmId: String)
}
