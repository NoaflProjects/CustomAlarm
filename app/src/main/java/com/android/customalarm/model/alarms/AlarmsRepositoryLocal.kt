package com.android.customalarm.model.alarms

import io.objectbox.Box
import io.objectbox.kotlin.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

/** Local implementation of [AlarmsRepository] using ObjectBox for storage */
class AlarmsRepositoryLocal(private val alarmBox: Box<AlarmEntity>) : AlarmsRepository {

  @OptIn(ExperimentalCoroutinesApi::class)
  override fun getAlarms(): Flow<List<Alarm>> =
      alarmBox
          .query()
          .build()
          .flow()
          .map { entities -> entities.map { it.toAlarm() } }
          .flowOn(Dispatchers.IO)

  @OptIn(ExperimentalCoroutinesApi::class)
  override fun getAlarmById(alarmId: String): Flow<Alarm?> =
      alarmBox
          .query()
          .build()
          .flow()
          .map { entities -> entities.firstOrNull { it.alarmId == alarmId }?.toAlarm() }
          .flowOn(Dispatchers.IO)

  override suspend fun addAlarm(alarm: Alarm) {
    alarmBox.put(alarm.toEntity())
  }

  override suspend fun removeAlarm(alarmId: String) {
    requireAlarmExists(alarmId)

    val entity = alarmBox.all.first { it.alarmId == alarmId }

    alarmBox.remove(entity.objectBoxId)
  }

  override suspend fun modifyAlarm(alarmId: String, alarm: Alarm) {
    requireAlarmExists(alarmId)

    val existing = alarmBox.all.first { it.alarmId == alarmId }

    val updated = alarm.copy(id = alarmId).toEntity().copy(objectBoxId = existing.objectBoxId)

    alarmBox.put(updated)
  }

  override suspend fun toggleAlarm(alarmId: String, enabled: Boolean) {
    requireAlarmExists(alarmId)

    val existing = alarmBox.all.first { it.alarmId == alarmId }

    alarmBox.put(existing.copy(isEnabled = enabled))
  }

  override suspend fun requireAlarmExists(alarmId: String) {
    require(alarmBox.all.any { it.alarmId == alarmId }) { "Alarm with ID $alarmId does not exist." }
  }
}
