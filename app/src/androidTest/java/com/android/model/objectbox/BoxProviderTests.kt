package com.android.model.objectbox

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.android.customalarm.model.alarms.AlarmEntity
import com.android.customalarm.model.objectbox.BoxProvider
import io.objectbox.Box
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BoxProviderTests {

  private lateinit var context: Context

  @Before
  fun setup() {
    context = ApplicationProvider.getApplicationContext()
  }

  @After
  fun tearDown() {
    BoxProvider.closeStore()
  }

  @Test
  fun init_and_alarmBox_returnsBox() {
    BoxProvider.init(context)

    val box: Box<AlarmEntity> = BoxProvider.alarmBox()

    assertNotNull(box)
    assertEquals(AlarmEntity::class.java, box.entityClass)
  }

  @Test
  fun alarmBox_withoutInit_throwsException() {
    // Manual cleanup to ensure uninitialized state
    BoxProvider.closeStore()

    assertThrows(IllegalStateException::class.java) { BoxProvider.alarmBox() }
  }
}
