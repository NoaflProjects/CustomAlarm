package com.android.customalarm.model.objectbox

import android.content.Context
import com.android.customalarm.model.alarms.AlarmEntity
import com.android.customalarm.model.alarms.MyObjectBox
import io.objectbox.Box
import io.objectbox.BoxStore

/** Singleton object to provide ObjectBox boxes for data storage */
object BoxProvider {

  private var store: BoxStore? = null

  /** Initialize the BoxStore with the application context */
  fun init(context: Context) {
    store = MyObjectBox.builder().androidContext(context.applicationContext).build()
  }

  /**
   * Returns the [Box] for [AlarmEntity], which is the interface to query, insert, update, and
   * remove alarms.
   *
   * Make sure [BoxProvider.init] has been called before using this.
   */
  fun alarmBox(): Box<AlarmEntity> {
    val currentStore =
        store
            ?: throw IllegalStateException(
                "BoxProvider is not initialized. Call BoxProvider.init(context) in Application.")
    return currentStore.boxFor(AlarmEntity::class.java)
  }

  /** Close the BoxStore */
  fun closeStore() {
    store?.close()
    store = null // Reset store to allow re-initialization
  }
}
