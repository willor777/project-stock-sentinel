package com.willor.sentinel_v2.presentation.scanner.scanner_components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.willor.lib_data.domain.dataobjs.entities.TriggerEntity
import com.willor.sentinel_v2.presentation.common.DotdotdotLoadingText
import com.willor.sentinel_v2.presentation.common.LabelValueRow
import com.willor.sentinel_v2.ui.theme.MySizes
import com.willor.sentinel_v2.utils.formatDoubleToTwoDecimalPlaceString
import com.willor.sentinel_v2.utils.formatTimestampToStringDateWithTime


@Composable
fun TriggerItem(
    trigger: TriggerEntity,
    searchTickerClick: () -> Unit,
){

    var expanded by remember{
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier.padding(
            vertical = MySizes.VERTICAL_CONTENT_PADDING_SMALL
        ),
        shape = RoundedCornerShape(MySizes.CARD_ROUNDED_CORNER),
        elevation = MySizes.CARD_ELEVATION
    ){
        Column(
            modifier = Modifier.animateContentSize(
                animationSpec = SpringSpec(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        ){

            ClickableHeaderRow(trigger) {
                expanded = !expanded
            }

            if (expanded){
                ExpandedContent(trigger)
            }
        }
    }
}


@Composable
private fun ClickableHeaderRow(
    trigger: TriggerEntity,
    onClick: () -> Unit,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .clickable(onClick = onClick)
            .background(Color.Red),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            modifier = Modifier.padding(horizontal = MySizes.HORIZONTAL_CONTENT_PADDING_MEDIUM),
            text = trigger.ticker,
            style = MaterialTheme.typography.titleSmall
        )

        Text(
            text = formatTimestampToStringDateWithTime(trigger.timestamp),
            style = MaterialTheme.typography.titleSmall
        )

        Text(
            modifier = Modifier.padding(horizontal = MySizes.HORIZONTAL_CONTENT_PADDING_MEDIUM),
            text = if (trigger.triggerValue == 1) "BUY" else "SELL",
            style = MaterialTheme.typography.titleSmall
        )
    }
}


@Composable
private fun ExpandedContent(
    trigger: TriggerEntity
){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(Color.DarkGray)
    ){

        // Price $
        LabelValueRow(
            label = "Price at time of Trigger:",
            value = "$${formatDoubleToTwoDecimalPlaceString(trigger.stockPriceAtTime)}"
        )

        LabelValueRow(label = "Current Price:") {
            DotdotdotLoadingText {
                false
            }
        }




    }
}











