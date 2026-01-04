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

  /**
   * Initializes this provider with the default production [AlarmsRepository] implementation.
   *
   * This method is intended to be called exactly once from the application's
   * [android.app.Application.onCreate] method. Tests should instead configure the repository by
   * assigning to [repository] directly.
   */
  fun initDefault() {
    if (_repository != null) {
      // Already initialized; avoid re-initialization.
      return
    }
    _repository = AlarmsRepositoryLocal()
  }
}
