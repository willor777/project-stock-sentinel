package com.willor.sentinel_v2.presentation.uoa.uoa_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.willor.lib_data.domain.dataobjs.responses.uoa_page_resp.UnusualOption
import com.willor.sentinel_v2.presentation.common.LabelValueRow
import com.willor.sentinel_v2.ui.theme.MySizes
import com.willor.sentinel_v2.utils.formatIntegerToString


@Composable
fun UoaItem(
    data: UnusualOption
) {

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
        Column {
            HeaderRow(
                ticker = data.ticker,
                type = data.contractType,
                strike = data.strikePrice,
                expiry = data.contractExpiry
            )

            Spacer(
                Modifier.height(10.dp)
            )    
            
            LabelValueRow(label = "Vol / Open Int Ratio:", value = data.volOiRatio.toString())
            LabelValueRow(label = "Volume:", value = formatIntegerToString(data.volume))
            LabelValueRow(label = "Open Interest:", value = formatIntegerToString(data.openInt))
            LabelValueRow(label = "Implied Volatility:", value = "%${data.impVol}")

            Spacer(
                Modifier.height(50.dp)
            )
        }
    }


//    val ask: Double,
//    val bid: Double,
//    val contractExpiry: String,
//    val contractType: String,
//    val impVol: Double,
//    val lastPrice: Double,
//    val openInt: Int,
//    val otmPercentage: Double,
//    val strikePrice: Double,
//    val ticker: String,
//    val volOiRatio: Double,
//    val volume: Int


}


@Composable
private fun HeaderRow(
    ticker: String,
    type: String,
    strike: Double,
    expiry: String
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
            .padding(horizontal = MySizes.HORIZONTAL_EDGE_PADDING),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "$ticker | $expiry | $displayType | ${trimDecimal(strike)}",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )

        Text(
            text = displayType,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )

//        Text(
//            text = ticker,
//            fontSize = MaterialTheme.typography.titleMedium.fontSize,
//            fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
//            fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
//            fontWeight = FontWeight.Bold,
//            color = Color.White
//        )
//
//        Text(
//            text = "$displayType ${trimDecimal(strike)}",
//            fontSize = MaterialTheme.typography.titleMedium.fontSize,
//            fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
//            fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
//            fontWeight = FontWeight.Bold,
//            color = Color.White
//        )
//
//        Text(
//            text = expiry,
//            fontSize = MaterialTheme.typography.titleMedium.fontSize,
//            fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
//            fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
//            fontWeight = FontWeight.Bold,
//            color = Color.White
//        )
    }
}


/**
 * Trims the zeros from double
 */
private fun trimDecimal(doubleValue: Double): String{
    return doubleValue.toString().substringBefore(".0")
}















