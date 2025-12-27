package com.android.customalarm.ui.alarms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.customalarm.model.alarms.Alarm
import com.android.customalarm.model.alarms.AlarmsRepository
import com.android.customalarm.model.alarms.AlarmsRepositoryProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlarmsViewModel(
    val alarmsRepository: AlarmsRepository = AlarmsRepositoryProvider.repository
) : ViewModel() {

  // StateFlow holding the list of alarms
  val alarms: StateFlow<List<Alarm>> =
      alarmsRepository
          .getAlarms()
          .stateIn(viewModelScope, SharingStarted.Companion.Lazily, emptyList())

  /** Add a new alarm */
  fun addAlarm(alarm: Alarm) = viewModelScope.launch { alarmsRepository.addAlarm(alarm) }

  /** Toggle enabled state */
  fun toggleAlarm(alarmId: String, enabled: Boolean) =
      viewModelScope.launch { alarmsRepository.toggleAlarm(alarmId, enabled) }
}
