package com.willor.sentinel_v2.presentation.quote.quote_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.responses.etf_quote_resp.EtfQuote
import com.willor.lib_data.domain.dataobjs.responses.stock_quote_resp.StockQuote
import com.willor.sentinel_v2.presentation.common.LabelValueRow
import com.willor.sentinel_v2.presentation.common.ValueRangeBar
import com.willor.sentinel_v2.presentation.common.ValueRangeBarSettings
import com.willor.sentinel_v2.ui.theme.MySizes
import com.willor.sentinel_v2.utils.*


@Composable
fun QuoteMainContent(
    quoteUiStateProvider: () -> QuoteUiState
) {

    val uiState = quoteUiStateProvider()

    val stockQuote = uiState.stockQuote
    val etfQuote = uiState.etfQuote

    when {
        stockQuote is DataResourceState.Success -> {
            StockQuoteContent(stockQuote = stockQuote.data)
        }
        etfQuote is DataResourceState.Success -> {
            EtfQuoteContent(etfQuote = etfQuote.data)
        }
        else -> {
            ErrorAnimation()
        }
    }

}


@Composable
private fun StockQuoteContent(
    stockQuote: StockQuote?
) {

    stockQuote.let {

        val quote = it!!.data

        val volRatio = calculateRatio(
            curValue = quote.volume.toDouble(), avgValue = quote.avgVolume.toDouble()
        )
        val volRatioColor = determineGainLossColor(volRatio)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {

            Spacer(modifier = Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_LARGE))

            // Header
            QuoteHeader(ticker = quote.ticker)

            Spacer(modifier = Modifier.height(5.dp))

            // Company Name
            LabelValueRow(
                label = "Company Name",
                value = quote.name
            )

            // Quote Last Update
            LabelValueRow(
                label = "Last Updated",
                value = dateStringFromTimestamp(quote.quoteTimeStamp)
            )

            // Last Price
            LabelValueRow(
                label = "Last Price",
                value = "$" + formatDoubleToTwoDecimalPlaceString(
                    determineLastPrice(quote.lastPriceRegMarket, quote.prepostPrice)
                )
            )

            // Change Dollar + Change Percent
            LabelValueRow(
                label = "Change",
                value = formatChangeDollarAndChangePct(
                    changeDollar = determineChangePrepostOrReg(
                        quote.changeDollarRegMarket, quote.prepostChangeDollar
                    ),
                    changePct = determineChangePrepostOrReg(
                        quote.changePctRegMarket, quote.prepostChangePct
                    )
                ),
                valueColor = determineGainLossColor(
                    determineChangePrepostOrReg(
                        quote.changePctRegMarket, quote.prepostChangePct
                    )
                )
            )

            // Volume
            LabelValueRow(
                label = "Volume",
                value = formatIntegerToString(quote.volume),
                labelSuperScript = "Today"
            )

            // Avg Volume
            LabelValueRow(
                label = "Average Volume",
                value = formatIntegerToString(quote.avgVolume),
                labelSuperScript = "30day"
            )

            // Volume / Avg Vol Ratio
            LabelValueRow(
                label = "Vol / Avg Vol Ratio",
                value = formatDoubleToTwoDecimalPlaceString(volRatio),
                valueColor = volRatioColor
            )

            // Previous Close
            LabelValueRow(
                label = "Previous Close",
                value = "$" + formatDoubleToTwoDecimalPlaceString(quote.prevClose),
            )

            // Todays Open
            LabelValueRow(
                label = "Today's Open",
                value = "$" + formatDoubleToTwoDecimalPlaceString(quote.openPrice),
            )

            // Beta 5 year
            LabelValueRow(
                label = "Beta",
                labelSuperScript = "5yr",
                value = quote.betaFiveYearMonthly.toString()
            )

            // EPS
            LabelValueRow(
                label = "EPS",
                labelSuperScript = "TTM",
                value = "$" + formatDoubleToTwoDecimalPlaceString(quote.epsTTM),
            )

            // Div Yield + Div Percentage
            LabelValueRow(
                label = "Dividends",
                value = "$${quote.forwardDivYieldValue} (% ${quote.forwardDivYieldPercentage})"
            )

            // One Year Est
            LabelValueRow(
                label = "One Year Est.",
                value = "$${quote.oneYearTargetEstimate}"
            )

            // Next Earnings Date
            LabelValueRow(
                label = "Next Earnings",
                labelSuperScript = "Approx.",
                value = quote.nextEarningsDate
            )

            // PE Ratio
            LabelValueRow(
                label = "PE Ratio",
                labelSuperScript = "TTM",
                value = quote.peRatioTTM.toString()
            )

            // Market Cap
            LabelValueRow(
                label = "Market Cap",
                value = quote.marketCapAbbreviatedString
            )

            // Days High / Low
            ValueRangeBar(
                settings = ValueRangeBarSettings
                    .DoubleValueRange(
                        modifier = Modifier.fillMaxWidth(),
                        highValue = quote.daysRangeHigh,
                        highLabel = "Day's High",
                        curValue = quote.lastPriceRegMarket,
                        lowValue = quote.daysRangeLow,
                        lowLabel = "Day's Low",
                        barHeight = 20.dp,
                        valueFontSize = 14.sp,
                        labelFontSize = 10.sp,
                        fontColor = MaterialTheme.colorScheme.onBackground,
                        fontFamily = MaterialTheme.typography.bodySmall.fontFamily!!,
                        showTopCurValue = true,
                        showMidPoint = false,
                    )
            )

            Spacer(Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_LARGE))

            // Year High / Low
            ValueRangeBar(
                settings = ValueRangeBarSettings
                    .DoubleValueRange(
                        modifier = Modifier.fillMaxWidth(),
                        highValue = quote.fiftyTwoWeekRangeHigh,
                        highLabel = "52wk High",
                        curValue = quote.lastPriceRegMarket,
                        lowValue = quote.fiftyTwoWeekRangeLow,
                        lowLabel = "52wk Low",
                        barHeight = 20.dp,
                        valueFontSize = 14.sp,
                        labelFontSize = 10.sp,
                        fontColor = MaterialTheme.colorScheme.onBackground,
                        fontFamily = MaterialTheme.typography.bodySmall.fontFamily!!,
                        showTopCurValue = false,
                        showMidPoint = true,
                    )
            )
        }
    }
}


@Composable
private fun EtfQuoteContent(
    etfQuote: EtfQuote?
) {

    etfQuote.let {

        val quote = it!!.data

        val volRatio = calculateRatio(
            curValue = quote.volume.toDouble(), avgValue = quote.avgVolume.toDouble()
        )
        val volRatioColor = determineGainLossColor(volRatio)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {

            Spacer(modifier = Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_LARGE))

            // Header
            QuoteHeader(ticker = quote.ticker)

            Spacer(modifier = Modifier.height(5.dp))

            // Company Name
            LabelValueRow(
                label = "Company Name",
                value = quote.name
            )

            // Quote Last Update
            LabelValueRow(
                label = "Last Updated",
                value = dateStringFromTimestamp(quote.quoteTimeStamp)
            )

            // Last Price
            LabelValueRow(
                label = "Last Price",
                value = "$" + formatDoubleToTwoDecimalPlaceString(
                    determineLastPrice(quote.lastPriceRegMarket, quote.prepostPrice)
                )
            )

            // Change Dollar + Change Percent
            LabelValueRow(
                label = "Change",
                value = formatChangeDollarAndChangePct(
                    changeDollar = determineChangePrepostOrReg(
                        quote.changeDollarRegMarket, quote.prepostChangeDollar
                    ),
                    changePct = determineChangePrepostOrReg(
                        quote.changePctRegMarket, quote.prepostChangePct
                    )
                ),
                valueColor = determineGainLossColor(
                    determineChangePrepostOrReg(
                        quote.changePctRegMarket, quote.prepostChangePct
                    )
                )
            )

            // Volume
            LabelValueRow(
                label = "Volume",
                value = formatIntegerToString(quote.volume),
                labelSuperScript = "Today"
            )

            // Avg Volume
            LabelValueRow(
                label = "Average Volume",
                value = formatIntegerToString(quote.avgVolume),
                labelSuperScript = "30day"
            )

            // Volume / Avg Vol Ratio
            LabelValueRow(
                label = "Vol / Avg Vol Ratio",
                value = formatDoubleToTwoDecimalPlaceString(volRatio),
                valueColor = volRatioColor
            )

            // Previous Close
            LabelValueRow(
                label = "Previous Close",
                value = "$" + formatDoubleToTwoDecimalPlaceString(quote.prevClose),
            )

            // Todays Open
            LabelValueRow(
                label = "Today's Open",
                value = "$" + formatDoubleToTwoDecimalPlaceString(quote.openPrice),
            )

            // Net Assets
            LabelValueRow(
                label = "Net Assets",
                value = quote.netAssetsAbbreviatedString
            )

            // NAV
            LabelValueRow(
                label = "NAV",
                value = quote.nav.toString()
            )

            // PE Ratio
            LabelValueRow(
                label = "PE Ratio",
                labelSuperScript = "TTM",
                value = quote.peRatioTTM.toString()
            )

            // Yield Percentage
            LabelValueRow(
                label = "Yield",
                value = "%${quote.yieldPercentage}"
            )

            // YTD Return
            LabelValueRow(
                label = "YTD Return",
                value = "%${quote.yearToDateTotalReturn}"
            )

            // Beta 5 year
            LabelValueRow(
                label = "Beta",
                labelSuperScript = "5yr",
                value = quote.betaFiveYearMonthly.toString()
            )

            // Net Expense Ratio
            LabelValueRow(
                label = "Net Expense Ratio",
                value = "%${quote.expenseRatioNetPercentage}"
            )

            // Inception Date
            LabelValueRow(
                label = "Inception Date",
                value = quote.inceptionDate
            )

            // Days High / Low
            ValueRangeBar(
                settings = ValueRangeBarSettings
                    .DoubleValueRange(
                        modifier = Modifier.fillMaxWidth(),
                        highValue = quote.daysRangeHigh,
                        highLabel = "Day's High",
                        curValue = quote.lastPriceRegMarket,
                        lowValue = quote.daysRangeLow,
                        lowLabel = "Day's Low",
                        barHeight = 20.dp,
                        valueFontSize = 14.sp,
                        labelFontSize = 10.sp,
                        fontColor = MaterialTheme.colorScheme.onBackground,
                        fontFamily = MaterialTheme.typography.bodySmall.fontFamily!!,
                        showTopCurValue = true,
                        showMidPoint = false,
                    )
            )

            Spacer(Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_LARGE))

            // Year High / Low
            ValueRangeBar(
                settings = ValueRangeBarSettings
                    .DoubleValueRange(
                        modifier = Modifier.fillMaxWidth(),
                        highValue = quote.fiftyTwoWeekRangeHigh,
                        highLabel = "52wk High",
                        curValue = quote.lastPriceRegMarket,
                        lowValue = quote.fiftyTwoWeekRangeLow,
                        lowLabel = "52wk Low",
                        barHeight = 20.dp,
                        valueFontSize = 14.sp,
                        labelFontSize = 10.sp,
                        fontColor = MaterialTheme.colorScheme.onBackground,
                        fontFamily = MaterialTheme.typography.bodySmall.fontFamily!!,
                        showTopCurValue = false,
                        showMidPoint = true,
                    )
            )
        }
    }
}


@Composable
private fun QuoteHeader(
    ticker: String
) {
    Text(
        text = "$ticker Base Quote",
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


@Composable
private fun ErrorAnimation() {

}


private fun determineLastPrice(regMarket: Double, prepost: Double): Double {
    return if (prepost != 0.0) prepost else regMarket
}


private fun determineChangePrepostOrReg(
    regMarketChangeDollar: Double,
    prepostChangeDollar: Double
): Double {
    return if (prepostChangeDollar != 0.0) prepostChangeDollar else regMarketChangeDollar
}
