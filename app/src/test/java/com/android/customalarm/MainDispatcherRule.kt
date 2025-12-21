package com.android.customalarm

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/** JUnit Rule to set Main dispatcher to a [TestDispatcher] for unit testing */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()) :
    TestWatcher() {

  /** Sets the Main dispatcher to the test dispatcher before each test */
  override fun starting(description: Description) {
    Dispatchers.setMain(dispatcher)
  }

  /** Resets the Main dispatcher to the original Main dispatcher after each test */
  override fun finished(description: Description) {
    Dispatchers.resetMain()
  }
}
