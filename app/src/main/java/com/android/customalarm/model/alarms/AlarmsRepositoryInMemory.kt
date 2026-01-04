package com.android.customalarm.model.alarms

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/** In-memory implementation of [AlarmsRepository] */
class AlarmsRepositoryInMemory : AlarmsRepository {
  private val _alarms = MutableStateFlow<List<Alarm>>(emptyList())

  override fun getAlarms(): Flow<List<Alarm>> = _alarms.asStateFlow()

  override fun getAlarmById(alarmId: String): Flow<Alarm?> {
    return _alarms.asStateFlow().map { alarms -> alarms.find { it.id == alarmId } }
  }

  override suspend fun addAlarm(alarm: Alarm) {
    // Check for duplicate alarm ID
    require(_alarms.value.none { it.id == alarm.id }) {
      "Alarm with ID ${alarm.id} already exists."
    }

    _alarms.value = _alarms.value + alarm
  }

  override suspend fun removeAlarm(alarmId: String) {
    requireAlarmExists(alarmId = alarmId)
    _alarms.value = _alarms.value.filterNot { it.id == alarmId }
  }

  override suspend fun modifyAlarm(alarmId: String, alarm: Alarm) {
    requireAlarmExists(alarmId = alarmId)
    _alarms.value = _alarms.value.map { if (it.id == alarmId) alarm.copy(id = alarmId) else it }
  }

  override suspend fun toggleAlarm(alarmId: String, enabled: Boolean) {
    requireAlarmExists(alarmId = alarmId)
    _alarms.value = _alarms.value.map { if (it.id == alarmId) it.copy(isEnabled = enabled) else it }
  }

  override suspend fun requireAlarmExists(alarmId: String) {
    require(_alarms.value.any { it.id == alarmId }) { "Alarm with ID $alarmId does not exist." }
  }
}
