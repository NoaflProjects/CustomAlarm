package com.android.customalarm.model.alarms

/** Provides a singleton instance of [AlarmsRepository] */
object AlarmsRepositoryProvider {
  private val _repository: AlarmsRepository by lazy { AlarmsRepositoryInMemory() }

  var repository: AlarmsRepository = _repository
}
