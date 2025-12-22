package com.android.ui.common

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.customalarm.ui.common.TopBar
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TopBarTests {

  @get:Rule val composeRule = createComposeRule()

  private val leftTag = "left"
  private val middleTag = "middle"
  private val rightTag = "right"

  private var leftClicked = false
  private var rightClicked = false

  @Before
  fun setup() {
    leftClicked = false
    rightClicked = false

    composeRule.setContent {
      TopBar(
          leftText = "Left",
          onLeftClick = { leftClicked = true },
          middleText = "Middle",
          rightText = "Right",
          onRightClick = { rightClicked = true },
          leftTestTag = leftTag,
          middleTestTag = middleTag,
          rightTestTag = rightTag)
    }
  }

  @Test
  fun topBar_displaysAllTexts() {
    composeRule.onNodeWithTag(leftTag).assertIsDisplayed()
    composeRule.onNodeWithTag(middleTag).assertIsDisplayed()
    composeRule.onNodeWithTag(rightTag).assertIsDisplayed()
  }

  @Test
  fun topBar_clicksTriggerCallbacks() {
    composeRule.onNodeWithTag(leftTag).performClick()
    composeRule.onNodeWithTag(rightTag).performClick()

    assert(leftClicked)
    assert(rightClicked)
  }
}
