package com.willor.sentinel_v2.presentation.quote.quote_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun QuoteTopBar(
    onNavIconClick: () -> Unit,
    onSettingsIconClick: () -> Unit,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colorScheme.primary),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        // Open Drawer Icon...
        IconButton(
            onClick = onNavIconClick
        ){
            Icon(Icons.Default.MenuOpen, "", tint = Color.White)
        }

        // Screen Header
        Text(
            text = "Quote",
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
            fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
            color = Color.White
        )

        // Settings Icon
        IconButton(
            onClick = onSettingsIconClick
        ){
            Icon(Icons.Filled.Settings, "settings-icon", tint = Color.White)
        }
    }

}