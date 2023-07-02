package com.willor.sentinel_v2.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun LabelValueRow(
    label: String,
    labelSuperScript: String = "",
    value: @Composable () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LabelText(label = label, labelSuperScript = labelSuperScript)

        value()
    }
}


@Composable
fun LabelValueRow(
    label: String,
    value: String,
    labelSuperScript: String = "",
    valueColor: Color? = null
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LabelText(label = label, labelSuperScript = labelSuperScript)

        ValueText(value = value, valueColor)
    }
}


@Composable
private fun LabelText(
    label: String,
    labelSuperScript: String
) {

    Row(
        modifier = Modifier.wrapContentSize()
    ) {
        Text(
            text = label,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            fontWeight = FontWeight.Bold,
            fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
            color = MaterialTheme.colorScheme.onSecondary,
        )

        Text(
            text = labelSuperScript,
            fontSize = 8.sp,
            fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
            fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.align(Alignment.Top)
        )
    }


}


@Composable
private fun ValueText(
    value: String,
    color: Color? = null
) {

    Text(
        text = value,
        fontSize = MaterialTheme.typography.titleSmall.fontSize,
        fontWeight = FontWeight.Bold,
        fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
        color = color ?: MaterialTheme.colorScheme.onSecondary,

        // These are all set so that if a company name is very long it will cut it down and ...
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(
            start = 20.dp,
        )

    )
}
