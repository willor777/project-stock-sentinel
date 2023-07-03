package com.willor.sentinel_v2

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

// This file is used for basic tests




fun main() {
    val test = "Jun 30, 2023, 8:00:00 AM"
    val dateFormat = "MMM dd, yyyy, hh:mm:ss aa"
    val sdf = SimpleDateFormat(dateFormat)

    val date = sdf.parse(test)

    println(date.time)
}



