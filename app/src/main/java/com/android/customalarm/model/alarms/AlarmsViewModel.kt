package com.android.customalarm.model.alarms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlarmsViewModel(
    val alarmsRepository: AlarmsRepository = AlarmsRepositoryProvider.repository
) : ViewModel() {

  // StateFlow holding the list of alarms
  val alarms: StateFlow<List<Alarm>> =
      alarmsRepository.getAlarms().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

  /** Add a new alarm */
  fun addAlarm(alarm: Alarm) = viewModelScope.launch { alarmsRepository.addAlarm(alarm) }

  /** Toggle enabled state */
  fun toggleAlarm(alarmId: String, enabled: Boolean) =
      viewModelScope.launch { alarmsRepository.toggleAlarm(alarmId, enabled) }
}
