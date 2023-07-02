package com.willor.sentinel_v2.presentation.quote.quote_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.responses.options_overview_resp.OptionsOverview
import com.willor.sentinel_v2.presentation.common.LabelValueRow
import com.willor.sentinel_v2.presentation.common.ValueRangeBar
import com.willor.sentinel_v2.presentation.common.ValueRangeBarSettings
import com.willor.sentinel_v2.ui.theme.GainGreen
import com.willor.sentinel_v2.ui.theme.LossRed
import com.willor.sentinel_v2.ui.theme.MySizes
import com.willor.sentinel_v2.utils.*


@Composable
fun QuoteOptionsOverview(
    quoteUiStateProvider: () -> QuoteUiState
){

    val optionsOverview = quoteUiStateProvider().optionsOverview

    when(optionsOverview){
        is DataResourceState.Success -> {
            OptionsOverviewContent(optionsOverview.data)
        }
        else -> {

        }
    }
}


@Composable
private fun OptionsOverviewContent(
    optionsOverview: OptionsOverview?
){

    optionsOverview.let {

        val data = it!!.data
        val volRatio = calculateRatio(
            data.optionVolumeToday.toDouble(), data.optionVolumeAvgThirtyDay.toDouble()
        )
        val volRatioColor = determineGainLossColor(volRatio)
        val oiRatio = calculateRatio(
            data.openInterestToday.toDouble(), data.openInterestThirtyDay.toDouble()
        )
        val oiRatioColor = determinePutCallVolRatioColor(oiRatio)

        val impVolColor = determineGainLossColor(data.impVolChangeToday)

        OptionOverviewHeader(ticker = data.ticker)

        Spacer(modifier = Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_LARGE))

        LabelValueRow(
            label = "Put/Call Volume Ratio",
            value = data.putCallVolumeRatio.toString(),
            valueColor = determinePutCallVolRatioColor(r = data.putCallVolumeRatio)
        )

        LabelValueRow(
            label = "Total Volume",
            labelSuperScript = "Today",
            value = formatIntegerToString(data.optionVolumeToday)
        )

        LabelValueRow(
            label = "Avg Volume",
            labelSuperScript = "30day",
            value = formatIntegerToString(data.optionVolumeAvgThirtyDay)
        )

        LabelValueRow(
            label = "Vol / Avg Vol Ratio",
            value = formatDoubleToTwoDecimalPlaceString(volRatio),
            valueColor = volRatioColor
        )

        LabelValueRow(
            label = "Put/Call OI Ratio",
            value = formatDoubleToTwoDecimalPlaceString(data.putCallOpenInterestRatio),
            valueColor = determinePutCallVolRatioColor(r = data.putCallOpenInterestRatio)
        )

        LabelValueRow(
            label = "Total OI",
            value = data.openInterestToday.toString(),
            labelSuperScript = "Today"
        )

        LabelValueRow(
            label = "Avg OI",
            value = data.openInterestThirtyDay.toString(),
            labelSuperScript = "30day"
        )

        LabelValueRow(
            label = "OI / Avg OI Ratio",
            value = formatDoubleToTwoDecimalPlaceString(oiRatio),
            valueColor = oiRatioColor
        )

        LabelValueRow(
            label = "Implied Volatility",
            value = "${data.impVol}% (${data.impVolChangeToday}%)",
            valueColor = impVolColor
        )

        LabelValueRow(
            label = "Historical Volatility",
            value = data.historicalVolatilityPercentage.toString() + "%"
        )

        LabelValueRow(
            label = "IV Percentile",
            value = "${data.ivPercentile}%"
        )

        LabelValueRow(
            label = "IV Rank",
            value = "${data.ivRank}%"
        )

        LabelValueRow(
            label = "IV High",
            labelSuperScript = "TTM",
            value = "${data.ivHighLastYear}% (${formatDateToStringMMDDYYYY(data.ivHighDate)})"
        )

        LabelValueRow(
            label = "IV Low",
            labelSuperScript = "TTM",
            value = "${data.ivLowLastYear}% (${formatDateToStringMMDDYYYY(data.ivLowDate)})"
        )

        ValueRangeBar(
            settings = ValueRangeBarSettings.DoubleValueRange(
                modifier = Modifier.fillMaxWidth(),
                highValue = data.ivHighLastYear,
                highLabel = "IV High TTM",
                curValue = data.impVol,
                lowValue = data.ivLowLastYear,
                lowLabel = "IV Low TTM",
                barHeight = 20.dp,
                valueFontSize = 14.sp,
                labelFontSize = 10.sp,
                fontColor = MaterialTheme.colorScheme.onBackground,
                fontFamily = MaterialTheme.typography.bodySmall.fontFamily!!,
                showTopCurValue = true,
                showMidPoint = true,
            )
        )


    }

}



@Composable
private fun OptionOverviewHeader(
    ticker: String
) {
    Text(
        text = "$ticker Options Overview",
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
        fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
        textAlign = TextAlign.Center
    )
}


@Composable         // This has to be a composable in order to use the MaterialTheme
private fun determinePutCallVolRatioColor(r: Double): Color {
    return if (r > 1.2) {
        LossRed
    }else if(r < .8) {
        GainGreen
    }else{
        MaterialTheme.colorScheme.onBackground
    }
}


























