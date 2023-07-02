package com.willor.sentinel_v2

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun SlideInOutAnimation(content: @Composable () -> Unit, content2: @Composable () -> Unit) {
    var currentContent by remember { mutableStateOf(content) }
    val transition = updateTransition(targetState = currentContent)

    val enterAnim = slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(durationMillis = 300)
    ) + fadeIn(initialAlpha = 0.3f)

    val exitAnim = slideOutHorizontally(
        targetOffsetX = { -it },
        animationSpec = tween(durationMillis = 300)
    ) + fadeOut()

    Crossfade(targetState = currentContent, modifier = Modifier.fillMaxSize()) { content ->
        Box(modifier = Modifier.fillMaxSize()) {
            content.invoke()
        }
    }
    Crossfade(
        modifier = Modifier.fillMaxSize(),
        targetState = transition.targetState,
        animationSpec = tween(durationMillis = 300)
    ) { content ->
        Box(modifier = Modifier.fillMaxSize()) {
            content.invoke()
        }
    }
}