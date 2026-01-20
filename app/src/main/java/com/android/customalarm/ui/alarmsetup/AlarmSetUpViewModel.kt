package com.android.customalarm.ui.alarmsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.customalarm.model.alarms.Alarm
import com.android.customalarm.model.alarms.AlarmsRepository
import com.android.customalarm.model.alarms.AlarmsRepositoryProvider
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/** Default values for the Alarm Setup screen */
data object AlarmSetUpDefaults {
  const val DEFAULT_HOUR = 12
  const val DEFAULT_MINUTE = 0
  const val DEFAULT_ALARM_NAME = ""
}

/**
 * Data class representing the UI state for the Alarm Setup screen.
 *
 * @param alarmId The unique identifier for the alarm being set up.
 * @param selectedHour The hour selected for the alarm.
 * @param selectedMinute The minute selected for the alarm.
 * @param alarmName The name given to the alarm.
 */
data class AlarmSetUpUiState(
    val alarmId: String = "",
    val selectedHour: Int = AlarmSetUpDefaults.DEFAULT_HOUR,
    val selectedMinute: Int = AlarmSetUpDefaults.DEFAULT_MINUTE,
    val alarmName: String = AlarmSetUpDefaults.DEFAULT_ALARM_NAME
)

/**
 * ViewModel for managing the state and logic of the Alarm Setup screen.
 *
 * @param alarmsRepository Repository for managing alarms.
 */
class AlarmSetUpViewModel(
    private val alarmsRepository: AlarmsRepository = AlarmsRepositoryProvider.repository
) : ViewModel() {
  // UI state for the Alarm Setup screen
  private val _uiState = MutableStateFlow(value = AlarmSetUpUiState())
  val uiState: StateFlow<AlarmSetUpUiState> = _uiState

  private val _isLoaded = MutableStateFlow(false)
  val isLoaded: StateFlow<Boolean> = _isLoaded

  /**
   * Use this function when creating a new alarm. Initializes the UI state with the current time.
   */
  fun createNewAlarm() {
    val now = java.util.Calendar.getInstance()
    _uiState.value =
        _uiState.value.copy(
            selectedHour = now[java.util.Calendar.HOUR_OF_DAY],
            selectedMinute = now[java.util.Calendar.MINUTE])
    _isLoaded.value = true
  }

  /** Call this to load an existing alarm for editing. */
  fun setAlarmId(alarmId: String) {
    _uiState.value = _uiState.value.copy(alarmId = alarmId)
    // Load existing alarm details if such an alarm exists in the repository
    viewModelScope.launch {
      val existingAlarm = alarmsRepository.getAlarmById(alarmId).firstOrNull()
      if (existingAlarm != null) {
        val timeParts = existingAlarm.time.split(":")
        val hour = timeParts.getOrNull(0)?.toIntOrNull() ?: AlarmSetUpDefaults.DEFAULT_HOUR
        val minute = timeParts.getOrNull(1)?.toIntOrNull() ?: AlarmSetUpDefaults.DEFAULT_MINUTE
        _uiState.value =
            _uiState.value.copy(
                selectedHour = hour, selectedMinute = minute, alarmName = existingAlarm.name)
      }
      _isLoaded.value = true
    }
  }

  // Change the selected time
  fun onTimeChanged(hour: Int, minute: Int) {
    _uiState.value = _uiState.value.copy(selectedHour = hour, selectedMinute = minute)
  }

  // Change the alarm name
  fun onAlarmNameChanged(name: String) {
    _uiState.value = _uiState.value.copy(alarmName = name)
  }

  // Save the alarm (add new or modify existing) in the repository
  fun saveAlarm() {
    viewModelScope.launch {
      val state = _uiState.value
      // Format time as "HH:MM"
      val time = String.format(Locale.US, "%02d:%02d", state.selectedHour, state.selectedMinute)
      val currentAlarm = alarmsRepository.getAlarmById(state.alarmId).firstOrNull()

      // If the alarm exists, modify it; otherwise, add a new alarm
      if (currentAlarm != null) {
        alarmsRepository.modifyAlarm(
            alarmId = state.alarmId, alarm = currentAlarm.copy(name = state.alarmName, time = time))
      } else {
        alarmsRepository.addAlarm(alarm = Alarm(name = state.alarmName, time = time))
      }
    }
  }

  // Delete the alarm from the repository
  fun deleteAlarm() {
    viewModelScope.launch {
      val alarmId = _uiState.value.alarmId

      // If there is no valid alarm ID, do nothing
      if (alarmId.isBlank()) {
        return@launch
      }

      alarmsRepository.removeAlarm(alarmId = alarmId)
    }
  }
}
