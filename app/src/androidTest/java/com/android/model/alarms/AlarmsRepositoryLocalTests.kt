package com.android.model.alarms

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.customalarm.model.alarms.Alarm
import com.android.customalarm.model.alarms.AlarmEntity
import com.android.customalarm.model.alarms.AlarmsRepositoryLocal
import com.android.customalarm.model.alarms.MyObjectBox
import io.objectbox.Box
import io.objectbox.BoxStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AlarmsRepositoryLocalTests {

  private lateinit var context: Context
  private lateinit var store: BoxStore
  private lateinit var alarmBox: Box<AlarmEntity>
  private lateinit var repository: AlarmsRepositoryLocal

  @Before
  fun setup() {
    context = ApplicationProvider.getApplicationContext()

    // Initialize ObjectBox BoxStore for testing
    store = MyObjectBox.builder().androidContext(context).directory(context.cacheDir).build()

    alarmBox = store.boxFor(AlarmEntity::class.java)
    repository = AlarmsRepositoryLocal(alarmBox)
  }

  @After
  fun tearDown() {
    store.close()
    store.deleteAllFiles()
  }

  @Test
  fun addAlarm_and_getAlarms_returnsAlarm() = runTest {
    val alarm = Alarm(name = "Test Alarm", time = "08:00")

    repository.addAlarm(alarm)

    val alarms = repository.getAlarms().first()

    Assert.assertEquals(1, alarms.size)
    Assert.assertEquals(alarm.id, alarms.first().id)
    Assert.assertEquals("Test Alarm", alarms.first().name)
  }

  @Test
  fun getAlarmById_returnsCorrectAlarm() = runTest {
    val alarm = Alarm(name = "Morning", time = "07:30")

    repository.addAlarm(alarm)

    val result = repository.getAlarmById(alarm.id).first()

    Assert.assertNotNull(result)
    Assert.assertEquals(alarm.id, result!!.id)
  }

  @Test
  fun removeAlarm_removesAlarm() = runTest {
    val alarm = Alarm(name = "Delete me")

    repository.addAlarm(alarm)
    repository.removeAlarm(alarm.id)

    val alarms = repository.getAlarms().first()

    Assert.assertTrue(alarms.isEmpty())
  }

  @Test(expected = IllegalArgumentException::class)
  fun removeAlarm_nonExisting_throwsException() = runTest { repository.removeAlarm("unknown-id") }

  @Test
  fun modifyAlarm_updatesAlarm() = runTest {
    val alarm = Alarm(name = "Old", time = "06:00")
    repository.addAlarm(alarm)

    val updated = alarm.copy(name = "New", time = "09:00")

    repository.modifyAlarm(alarm.id, updated)

    val result = repository.getAlarmById(alarm.id).first()

    Assert.assertEquals("New", result!!.name)
    Assert.assertEquals("09:00", result.time)
  }

  @Test
  fun toggleAlarm_updatesEnabledState() = runTest {
    val alarm = Alarm(isEnabled = false)
    repository.addAlarm(alarm)

    repository.toggleAlarm(alarm.id, true)

    val result = repository.getAlarmById(alarm.id).first()

    Assert.assertTrue(result!!.isEnabled)
  }

  @Test(expected = IllegalArgumentException::class)
  fun requireAlarmExists_nonExisting_throws() = runTest {
    repository.requireAlarmExists("does-not-exist")
  }
}
