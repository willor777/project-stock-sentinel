package com.willor.sentinel_v2.presentation.uoa.uoa_components

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.willor.lib_data.domain.dataobjs.UoaFilterOptions
import com.willor.lib_data.domain.dataobjs.UoaFilterOptions.*
import com.willor.lib_data.domain.dataobjs.responses.uoa_page_resp.UnusualOption
import com.willor.sentinel_v2.presentation.common.LabelValueRow
import com.willor.sentinel_v2.ui.theme.MySizes
import com.willor.sentinel_v2.utils.formatDoubleToTwoDecimalPlaceString
import com.willor.sentinel_v2.utils.formatIntegerToString


@Composable
fun UoaExpandableCard(
    data: UnusualOption,
    sortedBy: UoaFilterOptions,
    onSearchClicked: (ticker: String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(
                vertical = MySizes.VERTICAL_CONTENT_PADDING_SMALL,
                horizontal = MySizes.HORIZONTAL_EDGE_PADDING
            ),
        elevation = MySizes.CARD_ELEVATION,
        shape = RoundedCornerShape(MySizes.CARD_ROUNDED_CORNER)
    ) {
        Column(
            modifier = Modifier.animateContentSize(
                animationSpec = SpringSpec(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        ) {
            HeaderRow(
                ticker = data.ticker,
                type = data.contractType,
                strike = data.strikePrice,
                expiry = data.contractExpiry,
                sortedByValue = { determineSortedByValue(data, sortedBy) }
            ) {
                expanded = !expanded
            }

            if (expanded) {

                Column(
                    modifier = Modifier.padding(horizontal = MySizes.HORIZONTAL_EDGE_PADDING)
                ){
                    Spacer(
                        Modifier.height(5.dp)
                    )

                    LabelValueRow(
                        label = "Vol / Open Int Ratio:", value = data.volOiRatio.toString()
                    )
                    LabelValueRow(
                        label = "Volume:", value = formatIntegerToString(data.volume)
                    )
                    LabelValueRow(
                        label = "Open Interest:", value = formatIntegerToString(data.openInt)
                    )
                    LabelValueRow(
                        label = "Implied Volatility:", value = "${data.impVol}%"
                    )
                    LabelValueRow(
                        label = "OTM %:", value = "${data.otmPercentage}%"
                    )
                    LabelValueRow(
                        label = "Approx. Price:",
                        value = "$${formatDoubleToTwoDecimalPlaceString(data.lastPrice)}"
                    )

                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )
                }

            }


        }
    }

}

@Composable
private fun HeaderRow(
    ticker: String,
    type: String,
    strike: Double,
    expiry: String,
    sortedByValue: () -> String,
    onClick: () -> Unit
) {

    val color = if (type == "C") {
        Color(0xFF007676)
    } else {
        Color(0xFFB40068)
    }

    val displayType = if (type == "C") {
        "Call"
    } else {
        "Put"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(color)
            .padding(horizontal = MySizes.HORIZONTAL_EDGE_PADDING)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = ticker,
            style = MaterialTheme.typography.titleSmall,
            color = Color.White,
            modifier = Modifier.width(60.dp)
        )

        Text(
            text = expiry,
            style = MaterialTheme.typography.titleSmall,
            color = Color.White,
            modifier = Modifier.width(90.dp)
        )

        Text(
            text = displayType,
            style = MaterialTheme.typography.titleSmall,
            color = Color.White,
            modifier = Modifier.width(30.dp)
        )

        Text(
            text = trimDecimal(strike),
            style = MaterialTheme.typography.titleSmall,
            color = Color.White,
            modifier = Modifier.width(50.dp),
            textAlign = TextAlign.End
        )

        Text(
            text = sortedByValue(),
            style = MaterialTheme.typography.titleSmall,
            color = Color.White,
            modifier = Modifier.width(120.dp),
            textAlign = TextAlign.End
        )

    }
}


/**
 * Trims the zeros from double
 */
private fun trimDecimal(doubleValue: Double): String {
    return doubleValue.toString().substringBefore(".0")
}


/**
 * Formats option contract into "AAPL | 123.5 | C | 23-01-27"
 */
private fun formatOptionName(
    ticker: String, expiry: String, type: String, strike: String
): String {
    return "${ticker}_${strike}_${type}_${formatExpiryDateForOptionName(expiry)}"
        .replace("_", " | ")
}

/**
 * Format the expiry
 */
private fun formatExpiryDateForOptionName(expiry: String): String {
    return expiry.substringBefore("-").replace("20", "") + "-" +
            expiry.substringAfter('-')
}


/**
 * Determines value for sortedBy
 */
private fun determineSortedByValue(
    data: UnusualOption,
    sortedBy: UoaFilterOptions
): String {

    return when (sortedBy) {
        OTM_Percentage -> {
            data.otmPercentage.toString() + "%"
        }

        Last_Price -> {
            "$${formatDoubleToTwoDecimalPlaceString(data.lastPrice)}"
        }
        Volume -> {
            formatIntegerToString(data.volume)
        }
        Open_Int -> {
            formatIntegerToString(data.openInt)
        }
        Volume_OI_Ratio -> {
            data.volOiRatio.toString()
        }
        Implied_Volatility -> {
            data.impVol.toString() + "%"
        }
        else -> {
            // Rest of the values are on the header by default
            ""
        }
    }
}



















