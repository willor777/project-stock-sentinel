package com.willor.sentinel_v2.presentation.scanner.scanner_components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RunCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun StartScanner(
    startScannerClicked: () -> Unit
){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 32.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Start Sentinel:",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        IconButton(onClick = startScannerClicked) {
            Icon(
                Icons.Filled.RunCircle,
                contentDescription = "",
                modifier = Modifier.size(100.dp)
            )
        }

    }

}