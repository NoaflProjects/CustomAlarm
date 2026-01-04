package com.android.customalarm.model.alarms

/** Provides a singleton instance of [AlarmsRepository] */
object AlarmsRepositoryProvider {
  private var _repository: AlarmsRepository? = null

  // Repository instance, mutable for testing purposes
  var repository: AlarmsRepository
    // Getter throws if repository is not set (to catch uninitialized usage in tests)
    get() =
        _repository
            ?: throw IllegalStateException(
                "Repository not set. Use AlarmsRepositoryProvider.repository = ... in tests or initialize it in Application.")
    set(value) {
      _repository = value
    }

  /** Initialize with real repository (in Application) */
  fun initDefault() {
    if (_repository != null) {
      // Already initialized; avoid re-initialization.
      return
    }
    _repository = AlarmsRepositoryLocal()
  }
}
