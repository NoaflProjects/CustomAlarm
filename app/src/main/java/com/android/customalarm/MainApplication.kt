package com.android.customalarm

import android.app.Application
import com.android.customalarm.model.objectbox.BoxProvider

class MainApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    // Initialize Box database for local data storage
    BoxProvider.init(this)
  }
}
