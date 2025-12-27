package com.android.customalarm.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.android.customalarm.ui.theme.Dimens.paddingMassive
import com.android.customalarm.ui.theme.Dimens.paddingMedium

/**
 * A customizable top bar with optional left, middle, and right text elements.
 *
 * @param leftText Text to display on the left side.
 * @param onLeftClick Callback for left text click events.
 * @param middleText Text to display in the center.
 * @param rightText Text to display on the right side.
 * @param onRightClick Callback for right text click events.
 * @param leftTestTag Test tag for the left text element.
 * @param middleTestTag Test tag for the middle text element.
 * @param rightTestTag Test tag for the right text element.
 */
@Composable
fun TopBar(
    leftText: String = "",
    onLeftClick: () -> Unit = {},
    middleText: String = "",
    rightText: String = "",
    onRightClick: () -> Unit = {},
    leftTestTag: String = "",
    middleTestTag: String = "",
    rightTestTag: String = ""
) {
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .padding(
                  start = paddingMedium,
                  end = paddingMedium,
                  top = paddingMassive,
                  bottom = paddingMedium)) {
        if (leftText.isNotEmpty()) {
          Text(
              text = leftText,
              style = MaterialTheme.typography.bodySmall,
              modifier =
                  Modifier.align(Alignment.CenterStart)
                      .clickable(onClick = onLeftClick)
                      .then(
                          if (leftTestTag.isNotEmpty()) Modifier.testTag(leftTestTag)
                          else Modifier))
        }

        if (middleText.isNotEmpty()) {
          Text(
              text = middleText,
              style = MaterialTheme.typography.titleLarge,
              modifier =
                  Modifier.align(Alignment.Center)
                      .then(
                          if (middleTestTag.isNotEmpty()) Modifier.testTag(middleTestTag)
                          else Modifier))
        }

        if (rightText.isNotEmpty()) {
          Text(
              text = rightText,
              style = MaterialTheme.typography.bodySmall,
              modifier =
                  Modifier.align(Alignment.CenterEnd)
                      .clickable(onClick = onRightClick)
                      .then(
                          if (rightTestTag.isNotEmpty()) Modifier.testTag(rightTestTag)
                          else Modifier))
        }
      }
}
