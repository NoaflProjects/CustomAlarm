package com.android.customalarm.model.alarms

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * Data class representing an Alarm entity for local storage using ObjectBox.
 *
 * @param objectBoxId The unique ObjectBox ID for the entity.
 * @param alarmId The unique identifier for the alarm.
 * @param name The name of the alarm.
 * @param time The time of the alarm in HH:MM format.
 * @param isEnabled Boolean indicating if the alarm is enabled.
 */
@Entity
data class AlarmEntity(
    @Id var objectBoxId: Long = 0L,
    val alarmId: String,
    val name: String,
    val time: String,
    val isEnabled: Boolean
)
