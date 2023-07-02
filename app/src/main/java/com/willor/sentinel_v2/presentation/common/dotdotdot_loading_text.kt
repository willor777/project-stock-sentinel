package com.willor.sentinel_v2.presentation.common

import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive


@Composable
fun DotdotdotLoadingText(
    textStyle: TextStyle? = null,
    modifier: Modifier? = null,
    condition: () -> Boolean,

){
    var currentText by remember {
        mutableStateOf(".")
    }

    // Loop - Sleep .33 sec, check condition, if false-update dots, true-stop updating
    LaunchedEffect(true){
        while (this.isActive){
            delay(333L)
            if (!condition()){
                currentText = when (currentText) {
                    "   " -> {
                        ".  "
                    }
                    ".  " -> {
                        ".. "
                    }
                    ".. " -> {
                        "..."
                    }
                    else -> {
                        "   "
                    }
                }
            }
            else {
                break
            }
        }
    }

    when {
        modifier != null && textStyle != null -> {
            Text(
                text = currentText,
                style = textStyle,
                modifier = modifier
            )
        }
        modifier != null && textStyle == null -> {
            Text(
                text = currentText,
                modifier = modifier
            )
         }
        modifier == null && textStyle != null -> {
            Text(
                text = currentText,
                style = textStyle
            )
        }
        modifier == null && textStyle == null -> {
            Text(
                text = currentText
            )
        }

    }


}
