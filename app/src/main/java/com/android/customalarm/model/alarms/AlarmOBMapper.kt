package com.android.customalarm.model.alarms

/** Extension function to convert [AlarmEntity] to [Alarm] */
fun AlarmEntity.toAlarm(): Alarm =
    Alarm(id = alarmId, name = name, time = time, isEnabled = isEnabled)

/** Extension function to convert [Alarm] to [AlarmEntity] */
fun Alarm.toEntity(): AlarmEntity =
    AlarmEntity(alarmId = id, name = name, time = time, isEnabled = isEnabled)
