package com.android.customalarm.model.alarms

import java.util.UUID

/**
 * Data class representing an Alarm
 *
 * @param id Unique identifier for the alarm
 * @param name Name of the alarm
 * @param time Time of the alarm in HH:MM format
 * @param isEnabled Boolean indicating if the alarm is enabled
 */
data class Alarm(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "Alarm",
    val time: String = "07:00",
    val isEnabled: Boolean = false
)
